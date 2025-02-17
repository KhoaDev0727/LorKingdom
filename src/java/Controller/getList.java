/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.SuperCategory;
import Model.Category;
import Model.Age;
import Model.Sex;
import Model.PriceRange;
import Model.Brand;
import Model.Material;
import Model.Origin;

import DAO.OriginDAO;
import DAO.MaterialDAO;
import DAO.BrandDAO;
import DAO.PriceRangeDAO;
import DAO.SexDAO;
import DAO.AgeDAO;
import DAO.SuperCategoryDAO;
import DAO.CategoryDAO;

import static com.fasterxml.jackson.databind.util.ClassUtil.name;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author admin1
 */
public class getList extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet getList</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getList at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SuperCategoryDAO superCategoryDAO = new SuperCategoryDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        AgeDAO ageDAO = new AgeDAO();
        SexDAO sexDAO = new SexDAO();
        PriceRangeDAO priceRangeDAO = new PriceRangeDAO();
        BrandDAO brandDAO = new BrandDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        OriginDAO originDAO = new OriginDAO();
        
        
        try {

            List<SuperCategory> listSuperCategorys = superCategoryDAO.getAllSuperCategories();
            List<Category> listCategories = categoryDAO.getAllCategories();
            List<Age> listAges = ageDAO.getAllAges();
            List<Sex> listSex = sexDAO.getAllSexes();
            List<PriceRange> listPriceRange = priceRangeDAO.getAllPriceRanges();
            List<Brand> listBrand = brandDAO.getAllBrands();
            List<Material> materials = materialDAO.getAllMaterials();
            List<Origin> origins = originDAO.getAllOrigins();
            
            
            // Kiểm tra danh sách có dữ liệu hay không
            if (listSuperCategorys.isEmpty()) {
                System.out.println("Không có dữ liệu trong danh sách SuperCategory.");
            }
            if (listCategories.isEmpty()) {
                System.out.println("Không có dữ liệu trong danh sách Category.");
            }
            if (listAges.isEmpty()) {
                System.out.println(" có dữ liệu trong danh sách Age.");
            }
            if (listSex.isEmpty()) {
                System.out.println(" có dữ liệu trong danh sách Sex.");
            }
          
            // In ra console để kiểm tra dữ liệu trước khi truyền vào JSP
            System.out.println("Danh sách SuperCategory:");
            for (SuperCategory category : listSuperCategorys) {
                System.out.println("ID: " + category.getSuperCategoryID() + ", Name: " + category.getName());
            }

            System.out.println("Danh sách Category:");
            for (Category listCategory : listCategories) {
                System.out.println("ID: " + listCategory.getCategoryID() + ", Name: " + listCategory.getName());
            }

            System.out.println("Danh sách Age:");
            for (Age age : listAges) {
                System.out.println("ID: " + age.getAgeID() + ", Age Range: " + age.getAgeRange() + ", Created At: " + age.getCreatedAt());
            }

            System.out.println("Danh sách Sex:");
            for (Sex sex : listSex) {
                System.out.println("ID: " + sex.getSexID() + ", Name: " + sex.getName() + ", Created At: " + sex.getCreatedAt());
            }

            // Gửi dữ liệu đến JSP
            request.setAttribute("listPriceRanges", listPriceRange);
            request.setAttribute("superCategories", listSuperCategorys);
            request.setAttribute("categories", listCategories);
            request.setAttribute("ages", listAges);
            request.setAttribute("listS", listSex);
            request.setAttribute("listB", listBrand);
            request.setAttribute("listM", materials);
            request.setAttribute("listO", origins);
            
            request.getRequestDispatcher("home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi ra console
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
