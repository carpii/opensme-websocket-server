package backend.models;

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
	
	/** Position/order within the portfolio */
	private int pos;
	
	/** Stock symbol or identifier */
	private String symbol;
	
	/** Name of the stock/security */
	private String name;
	
	/** Type of position (long/short) */
	private String positionType;
	
	/** Date when item was added to portfolio */
	private Timestamp dateAdded;
	
	/** Currency of the security */
	private String currency;
	
	/** Purchase price of the security */
	private BigDecimal buyPrice;
	
	/** Number of shares held */
	private BigDecimal shares;
	
	/** Adjusted cost base per share */
	private BigDecimal acbPerShare;
	
	/** Last known price */
	private BigDecimal cachedLast;
	
	/** Target price for the security */
	private BigDecimal targetPrice;
	
	/** Data provider identifier */
	private String provider;
	
	/** Exchange where security is traded */
	private String exchange;
	
	/** User comments */
	private String comment;
	
	/** JSON override settings */
	private String overrides;
	
	/** UUID for transaction tracking */
	private String transactionUuid;
	
	/** Unique identifier for sync */
	private String uuid;
	
	/** Last update timestamp */
	private Timestamp lastUpdatedAt;

	/**
	 * Creates a new empty portfolio item.
	 */
	public PortfolioItem() {
		// Default constructor
	}

	/**
	 * Converts the portfolio item to a JSON object.
	 * 
	 * @return JSONObject representation of the portfolio item
	 */
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("portfolioId", portfolioId);
		json.put("pos", pos);
		json.put("symbol", symbol);
		json.put("name", name);
		json.put("positionType", positionType);
		json.put("dateAdded", dateAdded);
		json.put("currency", currency);
		json.put("buyPrice", buyPrice);
		json.put("shares", shares);
		json.put("acbPerShare", acbPerShare);
		json.put("cachedLast", cachedLast);
		json.put("targetPrice", targetPrice);
		json.put("provider", provider);
		json.put("exchange", exchange);
		json.put("comment", comment);
		json.put("overrides", overrides);
		json.put("transactionUuid", transactionUuid);
		json.put("uuid", uuid);
		json.put("lastUpdatedAt", lastUpdatedAt);
		return json;
	}

	// Add getters/setters
}