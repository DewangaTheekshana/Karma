package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
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
import org.hibernate.criterion.Order;

@WebServlet(name = "CheckSignin", urlPatterns = {"/CheckSignin"})
public class CheckSignin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JsonObject jsonObj = new JsonObject();
            Response_DTO response_DTO = new Response_DTO();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            Session session = HibernateUtil.getSessionFactory().openSession();

            if (request.getSession().getAttribute("user") != null) {
                //already Sign in
                User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                response_DTO.setSuccess(true);
                response_DTO.setContent(user_DTO);
            } else {
                response_DTO.setContent("Not Signed In");
            }
            jsonObj.add("response_DTO", gson.toJsonTree(response_DTO));

            Criteria criteria = session.createCriteria(Product.class);
            criteria.addOrder(Order.desc("id"));
            criteria.setMaxResults(3);

            List<Product> productList = criteria.list();

            for (Product product : productList) {
               product.setUser(null);
            }

            Gson gson1 = new Gson();
            jsonObj.add("products", gson1.toJsonTree(productList));

            response.setContentType("application/json");
            response.getWriter().write(gson1.toJson(jsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
