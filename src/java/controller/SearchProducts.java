package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Brand;
import entity.Category;
import entity.Product;
import entity.Size;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Get JSON request data
        JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);

        // Create product criteria
        Criteria criteria1 = session.createCriteria(Product.class);

        // Add sorting order by default
//        criteria1.addOrder(Order.desc("id"));

        // Add subcategory filter
        if (requestJsonObject.has("subcategory_name")) {
            String subcategory_name = requestJsonObject.get("subcategory_name").getAsString();

            // Get subcategory from the database
            Criteria criteria4 = session.createCriteria(Category.class);
            criteria4.add(Restrictions.eq("name", subcategory_name));
            Category subCategorylist = (Category) criteria4.uniqueResult();

            if (subCategorylist != null) {
                // Filter products by subcategory
                criteria1.add(Restrictions.eq("category", subCategorylist));
            }
        }

        // Add brand filter
        if (requestJsonObject.has("brand_name")) {
            String brand_name = requestJsonObject.get("brand_name").getAsString();

            // Get brand from the database
            Criteria criteria5 = session.createCriteria(Brand.class);
            criteria5.add(Restrictions.eq("name", brand_name));
            Brand brandlist = (Brand) criteria5.uniqueResult();

            if (brandlist != null) {
                // Filter products by brand
                criteria1.add(Restrictions.eq("brand", brandlist));
            }
        }

        // Add size filter
        if (requestJsonObject.has("size_name")) {
            String size_name = requestJsonObject.get("size_name").getAsString();

            // Get size from the database
            Criteria criteria6 = session.createCriteria(Size.class);
            criteria6.add(Restrictions.eq("name", size_name));
            Size sizeList = (Size) criteria6.uniqueResult();

            if (sizeList != null) {
                // Filter products by size
                criteria1.add(Restrictions.eq("size", sizeList));
            }
        }

        // Add price range filter
        if (requestJsonObject.has("price_range_start") && requestJsonObject.has("price_range_end")) {
            double price_range_start = requestJsonObject.get("price_range_start").getAsDouble();
            double price_range_end = requestJsonObject.get("price_range_end").getAsDouble();

            criteria1.add(Restrictions.ge("price", price_range_start));
            criteria1.add(Restrictions.le("price", price_range_end));
        }

        // Filter by sorting options
        if (requestJsonObject.has("sort_text")) {
            String sort_text = requestJsonObject.get("sort_text").getAsString();
            if (sort_text.equals("Sort by Latest")) {
                criteria1.addOrder(Order.desc("id"));
            } else if (sort_text.equals("Sort by Oldest")) {
                criteria1.addOrder(Order.asc("id"));
            } else if (sort_text.equals("Sort by Name")) {
                criteria1.addOrder(Order.asc("title"));
            } else if (sort_text.equals("Sort by Price")) {
                criteria1.addOrder(Order.asc("price"));
            }
        }

        // Pagination
        if (requestJsonObject.has("firstResult")) {
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            criteria1.setFirstResult(firstResult);
        }

        criteria1.setMaxResults(4); // Default max results

        // Get product list
        List<Product> productList = criteria1.list();
         responseJsonObject.addProperty("allProductCount", productList.size());

        // If no filters are selected, check if products are returned
        if (productList.isEmpty() && !requestJsonObject.has("subcategory_name") && !requestJsonObject.has("brand_name")
                && !requestJsonObject.has("size_name") && !requestJsonObject.has("price_range_start")) {
            productList = session.createCriteria(Product.class)
                    .setMaxResults(4)
                    .list(); // Fetch products without any filters
        }

        // Remove users from products before sending response
        for (Product product : productList) {
            product.setUser(null);
        }

        responseJsonObject.addProperty("success", true);
        responseJsonObject.add("productList", gson.toJsonTree(productList));

        // Send response
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJsonObject));
    }
}
