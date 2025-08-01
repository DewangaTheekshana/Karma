package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Category;
import entity.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadToSingleProduct", urlPatterns = {"/LoadToSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Response_DTO response_DTO = new Response_DTO();

        try {
            String productId = request.getParameter("id");

//            Validation.isEmailValid(productId)
            if (true) {
                Product product = (Product) session.get(Product.class, Integer.parseInt(productId));
                product.getUser().setPassword(null);
                product.getUser().setEmail(null);
                product.getUser().setVerification(null);

                Criteria criteria2 = session.createCriteria(Product.class);
                
                criteria2.add(Restrictions.eq("category", product.getCategory()));
                criteria2.add(Restrictions.eq("size", product.getSize()));
                criteria2.add(Restrictions.ne("id", product.getId()));
                criteria2.setMaxResults(4);

                List<Product> productList = criteria2.list();

                for (Product product1 : productList) {
                    product1.getUser().setPassword(null);
                    product1.getUser().setEmail(null);
                    product1.getUser().setVerification(null);

                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.add("Product", gson.toJsonTree(product));
                jsonObject.add("productList", gson.toJsonTree(productList));

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject));

            } else {
                response_DTO.setContent("product not fount");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}