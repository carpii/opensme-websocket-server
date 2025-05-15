package models;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a portfolio record in the PORTFOLIOS table.
 */
public class Portfolio {
	private int id;
	private int pos;
	private String name;
	private Timestamp dateAdded;
	private String description;
	private BigDecimal cash;
	private String currency;
	private BigDecimal defaultCosts;
	private String overrides;
	private String uuid;
	private Timestamp syncUpdatedAt;
	private Timestamp syncDeletedAt;
	private Timestamp syncPositionUpdatedAt;
	private boolean syncIgnore;

	/**
	 * Constructs a complete Portfolio instance.
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
}
