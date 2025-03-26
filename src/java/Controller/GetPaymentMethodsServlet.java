package Controller;

import DAO.PaymentDAO;
import Model.Payment;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetPaymentMethodsServlet extends HttpServlet {

    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        paymentDAO = new PaymentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Payment> paymentMethods = paymentDAO.getAllPaymentMethods();
            JSONArray jsonArray = new JSONArray();

            for (Payment method : paymentMethods) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", method.getPaymentMethodID());
                jsonObject.put("name", method.getMethodName());
                jsonObject.put("description", method.getDescription());
                jsonArray.put(jsonObject);
            }

            PrintWriter out = response.getWriter();
            out.print(jsonArray.toString());
            out.flush();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy dữ liệu phương thức thanh toán");
        }
    }
}