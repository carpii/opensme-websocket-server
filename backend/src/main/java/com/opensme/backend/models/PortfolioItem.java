package com.opensme.backend.models;

import org.json.JSONObject;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a portfolio item in the system.
 */
public class PortfolioItem {
    /** Unique identifier for the portfolio item */
    private int id;
    
    /** ID of the parent portfolio */
    private int portfolioId;
    
    /** Stock symbol or identifier */
    private String symbol;
    
    /** Number of shares held */
    private BigDecimal shares;
    
    /** Purchase price of the security */
    private BigDecimal buyPrice;
    
    /** Currency of the security */
    private String currency;
    
    /** Date the item was added */
    private Timestamp dateAdded;
    
    /** Comments/notes about the item */
    private String comment;  // Renamed from notes to match DB
    
    /** JSON overrides for this item */
    private String overrides;
    
    /** Last sync update timestamp */
    private Timestamp syncUpdatedAt;
    
    /** Sync deletion timestamp */
    private Timestamp syncDeletedAt;
    
    /** Last position sync timestamp */
    private Timestamp syncPositionUpdatedAt;

    /** Whether to ignore this item during sync */
    private boolean syncIgnore;

    /** Position in the portfolio display order */
    private int pos;
    
    /** Type of position (eg: long, short) */
    private String positionType;

    /**
     * Creates a new PortfolioItem
     * @param id Unique identifier
     * @param portfolioId Parent portfolio ID
     * @param symbol Stock symbol
     * @param shares Number of shares
     * @param buyPrice Purchase price
     * @param currency Currency code
     * @param dateAdded Date item was added
     * @param comment User notes
     * @param overrides Custom overrides
     * @param syncUpdatedAt Last sync update time
     * @param syncDeletedAt Deletion time
     * @param syncPositionUpdatedAt Position update time
     * @param syncIgnore Sync ignore flag
     * @param pos Position in list
     * @param positionType Type of position
     */
    public PortfolioItem(int id, int portfolioId, String symbol, 
                        BigDecimal shares, BigDecimal buyPrice, String currency,
                        Timestamp dateAdded, String comment,  // Renamed param
                        String overrides, Timestamp syncUpdatedAt, Timestamp syncDeletedAt,
                        Timestamp syncPositionUpdatedAt, boolean syncIgnore,
                        int pos, String positionType) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.symbol = symbol;
        this.shares = shares;
        this.buyPrice = buyPrice;
        this.currency = currency;
        this.dateAdded = dateAdded;
        this.comment = comment;  // Updated assignment
        this.overrides = overrides;
        this.syncUpdatedAt = syncUpdatedAt;
        this.syncDeletedAt = syncDeletedAt;
        this.syncPositionUpdatedAt = syncPositionUpdatedAt;
        this.syncIgnore = syncIgnore;
        this.pos = pos;
        this.positionType = positionType;
    }

    /**
     * Converts this item to JSON format
     * @return JSON representation
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("portfolio_id", portfolioId);
        json.put("symbol", symbol);
        json.put("shares", shares);
        json.put("buy_price", buyPrice);
        json.put("currency", currency);
        json.put("date_added", dateAdded != null ? dateAdded.toString() : JSONObject.NULL);
        json.put("comment", comment);
        json.put("overrides", overrides);
        json.put("sync_updated_at", syncUpdatedAt != null ? syncUpdatedAt.toString() : JSONObject.NULL);
        json.put("sync_deleted_at", syncDeletedAt != null ? syncDeletedAt.toString() : JSONObject.NULL);
        json.put("sync_position_updated_at", syncPositionUpdatedAt != null ? syncPositionUpdatedAt.toString() : JSONObject.NULL);
        json.put("sync_ignore", syncIgnore);
        json.put("pos", pos);
        json.put("position_type", positionType);
        return json;
    }

    /**
     * Creates a PortfolioItem from JSON
     * @param json JSON object to parse
     * @return New PortfolioItem instance
     */
    public static PortfolioItem fromJSON(JSONObject json) {
        return new PortfolioItem(
            json.getInt("id"),
            json.getInt("portfolio_id"),
            json.getString("symbol"),
            json.getBigDecimal("shares"),
            json.getBigDecimal("buy_price"),
            json.getString("currency"),
            json.has("date_added") ? Timestamp.valueOf(json.get("date_added").toString()) : null,
            json.has("comment") ? json.getString("comment") : null,
            json.has("overrides") ? json.getString("overrides") : null,
            json.has("sync_updated_at") ? Timestamp.valueOf(json.get("sync_updated_at").toString()) : null,
            json.has("sync_deleted_at") ? Timestamp.valueOf(json.get("sync_deleted_at").toString()) : null,
            json.has("sync_position_updated_at") ? Timestamp.valueOf(json.get("sync_position_updated_at").toString()) : null,
            json.getBoolean("sync_ignore"),
            json.getInt("pos"),
            json.getString("position_type")
        );
    }
}