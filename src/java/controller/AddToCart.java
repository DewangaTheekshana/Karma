package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author kv
 */
@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        String id = req.getParameter("id");
        String qty = req.getParameter("qty");

        System.out.println(id);
        System.out.println(qty);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            if (id.isEmpty()) {
                //product not found
                response_DTO.setContent("product not found");
            } else if (qty.isEmpty()) {
                //invalid qty
                response_DTO.setContent("invalid qty");
            } else {

                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

                if (productQty <= 0) {
                    //qty must be greater than 0
                    response_DTO.setContent("qty must be greater than 0");
                } else {

                    Product product = (Product) session.get(Product.class, productId);

                    if (product != null) {
                        //product found
                        response_DTO.setContent("product found");
                    } else {
                        //product not found
                        response_DTO.setContent("product not found");
                    }

                    if (req.getSession().getAttribute("user") != null) {
                        //DB cart

                        User_DTO user_DTO = (User_DTO) req.getSession().getAttribute("user");

                        //get db user
                        Criteria criteria1 = session.createCriteria(User.class);
                        criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                        User user = (User) criteria1.uniqueResult();

                        //check in db cart
                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        criteria2.add(Restrictions.eq("product", product));

                        if (criteria2.list().isEmpty()) {
                            //item not found in cart

                            if (productQty <= product.getQty()) {
                                //add product into cart

                                Cart cart = new Cart();
                                cart.setProduct(product);
                                cart.setQty(productQty);
                                cart.setUser(user);

                                session.save(cart);
                                transaction.commit();

                                response_DTO.setSuccess(true);
                                response_DTO.setContent("added product into cart");
                                System.out.println("added product into cart");

                            } else {
                                //qty not available
                                response_DTO.setContent("qty not available");
                            }

                        } else {
                            //item already found in cart

                            Cart cartItem = (Cart) criteria2.uniqueResult();

                            if ((cartItem.getQty() + productQty) <= product.getQty()) {

                                cartItem.setQty(cartItem.getQty() + productQty);
                                session.update(cartItem);
                                transaction.commit();

                                response_DTO.setSuccess(true);
                                response_DTO.setContent("cart updated");
                                System.out.println("cart updated");

                            } else {
                                // cantupdate

                                response_DTO.setContent("not available qty");
                            }

                        }

                    } else {
                        //session cart

                        HttpSession httpSession = req.getSession();

                        if (httpSession.getAttribute("sessionCart") != null) {
                            //session cart found
                            ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                            Cart_DTO foundCart_DTO = null;

                            for (Cart_DTO cart_DTO : sessionCart) {
                                if (cart_DTO.getProduct().getId() == product.getId()) {
                                    foundCart_DTO = cart_DTO;
                                    break;
                                }
                            }

                            if (foundCart_DTO != null) {
                                //product found
                                System.out.println("product found");

                                if ((foundCart_DTO.getQty() + productQty) <= product.getQty()) {
                                    //update qty

                                    foundCart_DTO.setQty(foundCart_DTO.getQty() + productQty);

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("cart updated");

                                } else {
                                    //qty not available
                                    response_DTO.setContent("qty not available");
                                }

                            } else {
                                //product not found

                                if (productQty < +product.getQty()) {
                                    //add to session cart
                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setProduct(product);
                                    cart_DTO.setQty(productQty);
                                    sessionCart.add(cart_DTO);

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("cart updated");
                                } else {
                                    //qty not available
                                    response_DTO.setContent("qty not available");
                                }

                            }

                        } else {
                            //add cart not found
                            System.out.println("add cart not found");

                            if (productQty <= product.getQty()) {
                                //add to session cart

                                ArrayList<Cart_DTO> sessionCart = new ArrayList<>();

                                Cart_DTO cart_DTO = new Cart_DTO();
                                cart_DTO.setProduct(product);
                                cart_DTO.setQty(productQty);
                                sessionCart.add(cart_DTO);

                                httpSession.setAttribute("sessionCart", sessionCart);

                                response_DTO.setSuccess(true);
                                response_DTO.setContent("cart updated");

                            } else {
                                //quantity not available
                                System.out.println("quantity not available");
                                response_DTO.setContent("quantity not available");
                            }

                        }

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(response_DTO));
        System.out.println(response_DTO.getContent());

    }

}
