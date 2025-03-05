/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAO.*;
import Model.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 *
 * @author admin1
 */
public class UpdateProductDataLoader {

    public static void loadDataForUpdate(HttpServletRequest request, int productID) throws ClassNotFoundException {
        // 1. Lấy product theo productID
        Product productFromDB = ProductDAO.getProductById(productID);
        request.setAttribute("product", productFromDB);

        // 2. Lấy danh sách cần thiết
        CategoryDAO categoryDAO = new CategoryDAO();
        BrandDAO brandDAO = new BrandDAO();
        AgeDAO ageDAO = new AgeDAO();
        SexDAO sexDAO = new SexDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        PriceRangeDAO priceRangeDAO = new PriceRangeDAO();
        OriginDAO originDAO = new OriginDAO();

        try {
            List<Category> listCategories = categoryDAO.getAllCategories();
            List<Brand> listBrands = brandDAO.getActiveBrand();
            List<Age> listAges = ageDAO.getAllAges();
            List<Sex> listGenders = sexDAO.getActiveSex();
            List<Material> listMaterials = materialDAO.getAllActiveMaterials();
            List<PriceRange> listPriceRanges = priceRangeDAO.getAllActivePriceRanges();
            List<Origin> listOrigins = originDAO.getAllActiveOrigins();

            // 3. Đưa vào request
            request.setAttribute("Categories", listCategories);
            request.setAttribute("brands", listBrands);
            request.setAttribute("ages", listAges);
            request.setAttribute("sexes", listGenders);
            request.setAttribute("materials", listMaterials);
            request.setAttribute("priceRanges", listPriceRanges);
            request.setAttribute("listOrigin", listOrigins);
        } catch (Exception e) {
        }
    }
}
