package com.opensme.backend.models;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a portfolio record in the PORTFOLIOS table.
 */
public class Portfolio {

    /**
     * Portfolio ID
     */
    private int id;

    /**
     * Position/order in the list
     */
    private int pos;

    /**
     * Portfolio name
     */
    private String name;

    /**
     * Date the portfolio was added
     */
    private Timestamp dateAdded;

    /**
     * Portfolio description
     */
    private String description;

    /**
     * Cash balance
     */
    private BigDecimal cash;

    /**
     * Currency code (e.g., USD, EUR)
     */
    private String currency;

    /**
     * Default transaction costs
     */
    private BigDecimal defaultCosts;

    /**
     * Portfolio overrides JSON
     */
    private String overrides;

    /**
     * Unique identifier
     */
    private String uuid;

    /**
     * Last sync update timestamp
     */
    private Timestamp syncUpdatedAt;

    /**
     * Sync deletion timestamp
     */
    private Timestamp syncDeletedAt;

    /**
     * Last position sync timestamp
     */
    private Timestamp syncPositionUpdatedAt;

    /**
     * Whether to ignore during sync
     */
    private boolean syncIgnore;

    /**
     * Creates a new Portfolio with the specified parameters.
     *
     * @param id Portfolio ID
     * @param pos Position in list
     * @param name Portfolio name
     * @param dateAdded Date added
     * @param description Portfolio description
     * @param cash Cash balance
     * @param currency Currency code
     * @param defaultCosts Default transaction costs
     * @param overrides Portfolio overrides JSON
     * @param uuid Unique identifier
     * @param syncUpdatedAt Last sync update timestamp
     * @param syncDeletedAt Sync deletion timestamp
     * @param syncPositionUpdatedAt Last position sync timestamp
     * @param syncIgnore Whether to ignore during sync
     */
    public Portfolio(int id, int pos, String name, Timestamp dateAdded, String description,
                     BigDecimal cash, String currency, BigDecimal defaultCosts, String overrides,
                     String uuid, Timestamp syncUpdatedAt, Timestamp syncDeletedAt,
                     Timestamp syncPositionUpdatedAt, boolean syncIgnore) {
        this.id = id;
        this.pos = pos;
        this.name = name;
        this.dateAdded = dateAdded;
        this.description = description;
        this.cash = cash;
        this.currency = currency;
        this.defaultCosts = defaultCosts;
        this.overrides = overrides;
        this.uuid = uuid;
        this.syncUpdatedAt = syncUpdatedAt;
        this.syncDeletedAt = syncDeletedAt;
        this.syncPositionUpdatedAt = syncPositionUpdatedAt;
        this.syncIgnore = syncIgnore;
    }

    /**
     * Serializes the portfolio to a JSON object.
     *
     * @return JSONObject representation of the portfolio
     */
    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("pos", pos);
        obj.put("name", name);
        obj.put("date_added", dateAdded != null ? dateAdded.toString() : JSONObject.NULL);
        obj.put("description", description != null ? description : JSONObject.NULL);
        obj.put("cash", cash);
        obj.put("currency", currency);
        obj.put("default_costs", defaultCosts);
        obj.put("overrides", overrides != null ? overrides : JSONObject.NULL);
        obj.put("uuid", uuid);
        obj.put("sync_updated_at", syncUpdatedAt != null ? syncUpdatedAt.toString() : JSONObject.NULL);
        obj.put("sync_deleted_at", syncDeletedAt != null ? syncDeletedAt.toString() : JSONObject.NULL);
        obj.put("sync_position_updated_at", syncPositionUpdatedAt != null ? syncPositionUpdatedAt.toString() : JSONObject.NULL);
        obj.put("sync_ignore", syncIgnore);
        return obj;
    }

    /**
     * Creates a Portfolio instance from a JSON object.
     */
    public static Portfolio fromJSON(JSONObject json) {
        return new Portfolio(
            json.getInt("id"),
            json.getInt("pos"),
            json.getString("name"),
            json.has("date_added") ? Timestamp.valueOf(json.getString("date_added")) : null,
            json.getString("description"),
            json.getBigDecimal("cash"),
            json.getString("currency"),
            json.getBigDecimal("default_costs"),
            json.getString("overrides"),
            json.getString("uuid"),
            json.has("sync_updated_at") ? Timestamp.valueOf(json.getString("sync_updated_at")) : null,
            json.has("sync_deleted_at") ? Timestamp.valueOf(json.getString("sync_deleted_at")) : null,
            json.has("sync_position_updated_at") ? Timestamp.valueOf(json.getString("sync_position_updated_at")) : null,
            json.getBoolean("sync_ignore")
        );
    }

    public int getId() {
        return id;
    }

    /** 
     * Gets the portfolio name.
     * @return the name of this portfolio
     */
    public String getName() { 
        return name; 
    }

    public static List<Portfolio> getDummyPortfolios() {
        List<Portfolio> portfolios = new ArrayList<>();
        portfolios.add(new Portfolio(
            1, 1, "Sample Portfolio", 
            new Timestamp(System.currentTimeMillis()),
            "Test portfolio", 
            new BigDecimal("10000.00"),
            "USD",
            new BigDecimal("9.99"),
            "{}",
            "sample-uuid",
            null, null, null, false
        ));
        return portfolios;
    }
}
