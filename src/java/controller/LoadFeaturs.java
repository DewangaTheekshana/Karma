/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.Size;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
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

/**
 *
 * @author kv
 */
@WebServlet(name = "LoadFeaturs", urlPatterns = {"/LoadFeaturs"})
public class LoadFeaturs extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("name"));
        System.out.println(criteria1.addOrder(Order.asc("name")));
        List<Category> categoryList = criteria1.list();

        Criteria criteria2 = session.createCriteria(Brand.class);
        criteria2.addOrder(Order.asc("name"));
        List<Category> brandList = criteria2.list();
        
        Criteria criteria4 = session.createCriteria(Size.class);
        criteria4.addOrder(Order.asc("id"));
        List<Category> sizeList = criteria4.list();
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("brandList", gson.toJsonTree(brandList));
        jsonObject.add("sizeList", gson.toJsonTree(sizeList));
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(jsonObject));

    }

}
