package models;

import org.json.JSONArray;
import org.json.JSONObject;
import util.DatabaseHelper; // Import DatabaseHelper

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a portfolio group entity.
 */
public class PortfolioGroup {
    /**
     * Portfolio group ID
     */
    private int id;

    /**
     * Group name
     */
    private String name;

    /**
     * Group description
     */
    private String description;

    /**
     * Currency code (e.g., USD, EUR)
     */
    private String currency;

    /**
     * Date the group was added
     */
    private Timestamp dateAdded;

    /**
     * Last sync update timestamp
     */
    private Timestamp syncUpdatedAt;

    /**
     * Sync deletion timestamp
     */
    private Timestamp syncDeletedAt;

    /**
     * Whether to ignore during sync
     */
    private boolean syncIgnore;

    /**
     * Position/order in the list
     */
    private int pos;

    /**
     * Creates a new PortfolioGroup instance.
     */
    public PortfolioGroup() {}

    // Add Javadoc for all getters/setters
    /**
     * Gets the portfolio group ID.
     * @return the ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the portfolio group ID.
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the group name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the group name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the group description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the group description.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the currency code.
     * @return the currency code
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency code.
     * @param currency the currency code to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Gets the date the group was added.
     * @return the date added
     */
    public Timestamp getDateAdded() {
        return dateAdded;
    }

    /**
     * Sets the date the group was added.
     * @param dateAdded the date to set
     */
    public void setDateAdded(Timestamp dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * Gets the last sync update timestamp.
     * @return the sync update timestamp
     */
    public Timestamp getSyncUpdatedAt() {
        return syncUpdatedAt;
    }

    /**
     * Sets the last sync update timestamp.
     * @param syncUpdatedAt the timestamp to set
     */
    public void setSyncUpdatedAt(Timestamp syncUpdatedAt) {
        this.syncUpdatedAt = syncUpdatedAt;
    }

    /**
     * Gets the sync deletion timestamp.
     * @return the sync deletion timestamp
     */
    public Timestamp getSyncDeletedAt() {
        return syncDeletedAt;
    }

    /**
     * Sets the sync deletion timestamp.
     * @param syncDeletedAt the timestamp to set
     */
    public void setSyncDeletedAt(Timestamp syncDeletedAt) {
        this.syncDeletedAt = syncDeletedAt;
    }

    /**
     * Checks whether to ignore during sync.
     * @return true if ignored during sync, false otherwise
     */
    public boolean isSyncIgnore() {
        return syncIgnore;
    }

    /**
     * Sets whether to ignore during sync.
     * @param syncIgnore true to ignore during sync, false otherwise
     */
    public void setSyncIgnore(boolean syncIgnore) {
        this.syncIgnore = syncIgnore;
    }

    /**
     * Gets the position/order in the list.
     * @return the position
     */
    public int getPos() {
        return pos;
    }

    /**
     * Sets the position/order in the list.
     * @param pos the position to set
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * Converts the PortfolioGroup object to a JSON representation.
     *
     * @return JSONObject representing the portfolio group
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("description", description);
        json.put("currency", currency);
        json.put("dateAdded", dateAdded);
        json.put("syncUpdatedAt", syncUpdatedAt);
        json.put("syncDeletedAt", syncDeletedAt);
        json.put("syncIgnore", syncIgnore);
        json.put("pos", pos);
        return json;
    }

    /**
     * Lists all portfolio groups from the database.
     *
     * @return JSONArray representing the list of portfolio groups
     */
    private JSONArray listPortfolioGroups() {
        List<PortfolioGroup> groups = new ArrayList<>();
        try (
            Connection conn = DatabaseHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PORTFOLIO_GROUPS");
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                PortfolioGroup group = new PortfolioGroup();
                group.setId(rs.getInt("ID"));
                group.setName(rs.getString("NAME"));
                group.setDescription(rs.getString("DESCRIPTION"));
                group.setCurrency(rs.getString("CURRENCY"));
                group.setDateAdded(rs.getTimestamp("DATE_ADDED"));
                group.setSyncUpdatedAt(rs.getTimestamp("SYNC_UPDATED_AT"));
                group.setSyncDeletedAt(rs.getTimestamp("SYNC_DELETED_AT"));
                group.setSyncIgnore(rs.getBoolean("SYNC_IGNORE"));
                group.setPos(rs.getInt("POS"));
                groups.add(group);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage(), e);
        }

        JSONArray jsonArray = new JSONArray();
        for (PortfolioGroup group : groups) {
            jsonArray.put(group.toJSON());
        }
        return jsonArray;
    }
}