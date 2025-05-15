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
    private int id;
    private String name;
    private String description;
    private String currency;
    private Timestamp dateAdded;
    private Timestamp syncUpdatedAt;
    private Timestamp syncDeletedAt;
    private boolean syncIgnore;
    private int pos;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Timestamp getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Timestamp dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Timestamp getSyncUpdatedAt() {
        return syncUpdatedAt;
    }

    public void setSyncUpdatedAt(Timestamp syncUpdatedAt) {
        this.syncUpdatedAt = syncUpdatedAt;
    }

    public Timestamp getSyncDeletedAt() {
        return syncDeletedAt;
    }

    public void setSyncDeletedAt(Timestamp syncDeletedAt) {
        this.syncDeletedAt = syncDeletedAt;
    }

    public boolean isSyncIgnore() {
        return syncIgnore;
    }

    public void setSyncIgnore(boolean syncIgnore) {
        this.syncIgnore = syncIgnore;
    }

    public int getPos() {
        return pos;
    }

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