package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Brand;
import entity.Category;
import entity.Product;
import entity.Size;
import entity.User;
import entity.product_status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        String categoryId = request.getParameter("categoryId");
        String brandId = request.getParameter("brandId");
        String title = request.getParameter("titleId");
        String description = request.getParameter("descriptionId");
        String sizeId = request.getParameter("sizeId");
        String price = request.getParameter("price");
        String quantity = request.getParameter("qty");

        Part image1 = request.getPart("image1");
        Part image2 = request.getPart("image2");
        Part image3 = request.getPart("image3");

        Session session = HibernateUtil.getSessionFactory().openSession();
        if (!Validations.isInteger(categoryId)) {
            response_DTO.setContent("Invalid Category");
        } else if (!Validations.isInteger(brandId)) {
            response_DTO.setContent("Invalid brand");
        } else if (title.isEmpty()) {
            response_DTO.setContent("Please Put Suitable title");
        } else if (description.isEmpty()) {
            response_DTO.setContent("Please Put Suitable Description");

        } else if (!Validations.isInteger(sizeId)) {
            response_DTO.setContent("Invalid Size Type");
        } else if (price.isEmpty()) {
            response_DTO.setContent("Please fill the price");
        } else if (!Validations.isDouble(price)) {
            response_DTO.setContent("Invalid Price");
        } else if (Double.parseDouble(price) <= 0) {
            response_DTO.setContent("Price must be greater than 0");

        } else if (quantity.isEmpty()) {
            response_DTO.setContent("Please Fill the QTY");
        } else if (!Validations.isInteger(quantity)) {
            response_DTO.setContent("Invalid Quantity");
        } else if (Integer.parseInt(quantity) <= 0) {
            response_DTO.setContent("Quantity must be greater than 0");

        } else if (image1.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload Image 1");
        } else if (image2.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload Image 2");
        } else if (image3.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload Image 3");
        } else {
            Category category = (Category) session.load(Category.class, Integer.parseInt(categoryId));

            if (category == null) {
                response_DTO.setContent("Please Select a valid Category");
            } else {
                Brand brand = (Brand) session.load(Brand.class, Integer.parseInt(brandId));
                if (brand == null) {
                    response_DTO.setContent("Please Select a valid Model");
                } else {
                    Size size = (Size) session.load(Size.class, Integer.parseInt(sizeId));
                    if (size == null) {
                        response_DTO.setContent("Please Select a valid size Type");
                    } else {
                        Product product = new Product();

                        product.setCategory(category);
                        product.setSize(size);
                        product.setDate_time(new Date());
                        product.setDescription(description);
                        product.setBrand(brand);
                        product.setPrice(Double.parseDouble(price));

                        //Get Active Status
                        product_status product_status = (product_status) session.load(product_status.class, 1);
                        product.setProduct_status(product_status);

                        product.setQty(Integer.parseInt(quantity));
                        product.setTitle(title);

                        //Get User
                        User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                        Criteria criteria1 = session.createCriteria(User.class);
                        criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                        User user = (User) criteria1.uniqueResult();
                        product.setUser(user);

                        int pid = (int) session.save(product);
                        session.beginTransaction().commit();

                        String applicatinPath = request.getServletContext().getRealPath("");
                        String newApplicationPath = applicatinPath.replace("build" + File.separator + "web", "web");

                        File folder = new File(newApplicationPath + "//product-images//" + pid);

                        folder.mkdir();

                        File file1 = new File(folder, "image1.png");
                        InputStream inputStream1 = image1.getInputStream();
                        Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        File file2 = new File(folder, "image2.png");
                        InputStream inputStream2 = image2.getInputStream();
                        Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        File file3 = new File(folder, "image3.png");
                        InputStream inputStream3 = image3.getInputStream();
                        Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        response_DTO.setSuccess(true);
                        response_DTO.setContent("New Product Added");
                    }
                }
            }
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
        session.close();
    }

}
