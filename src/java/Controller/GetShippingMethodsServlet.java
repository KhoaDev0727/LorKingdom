package Controller;

import DAO.ShippingDAO;
import Model.Shipping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetShippingMethodsServlet extends HttpServlet {

    private ShippingDAO shippingDAO;

    @Override
    public void init() throws ServletException {
        shippingDAO = new ShippingDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Shipping> shippingMethods = shippingDAO.getAllShippingMethods();
            JSONArray jsonArray = new JSONArray();

            for (Shipping method : shippingMethods) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", method.getShippingMethodID());
                jsonObject.put("name", method.getMethodName());
                jsonObject.put("description", method.getDescription());
                jsonArray.put(jsonObject);
            }

            PrintWriter out = response.getWriter();
            out.print(jsonArray.toString());
            out.flush();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy dữ liệu phương thức vận chuyển");
        }
    }
}