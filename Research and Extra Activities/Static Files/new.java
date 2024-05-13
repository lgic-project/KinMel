import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class CartManager {
    private RequestQueue requestQueue;
    private String cartUrl = "https://example.com/addToCart";

    public CartManager(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void addToCart(String itemId, int quantity, final Response.Listener<String> listener, final Response.ErrorListener errorListener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemId", itemId);
            jsonObject.put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, cartUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            listener.onResponse(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorListener.onErrorResponse(new VolleyError("Invalid response"));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                });

        requestQueue.add(request);
    }
}