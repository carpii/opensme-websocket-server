package backend.models;

import org.json.JSONObject;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PortfolioItem {
    private int id;
    private int portfolioId;
    private int pos;
    private String symbol;
    private String name;
    private String positionType;
    private Timestamp dateAdded;
    private String currency;
    private BigDecimal buyPrice;
    private BigDecimal shares;
    private BigDecimal acbPerShare;
    private BigDecimal cachedLast;
    private BigDecimal targetPrice;
    private String provider;
    private String exchange;
    private String comment;
    private String overrides;
    private String transactionUuid;
    private String uuid;
    private Timestamp lastUpdatedAt;

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