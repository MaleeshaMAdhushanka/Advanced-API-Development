import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet (urlPatterns = "/customer")
public class CustomerServelet extends HttpServlet {

    private final List<CustomerDTO> customerDTOList = new ArrayList<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");

        if (id == null || name == null || address == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid Input. ID Or Name Or Address cannot be empty\"}");
        }else {
        /*CustomerDTO customerDTO = new CustomerDTO(id, name, address);
        customerDTOList.add(customerDTO);*/


            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");

        try{
            CustomerDTO customerDTO = findById(id);
            if (customerDTO == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else {
                customerDTO.setName(name);
                customerDTO.setAddress(address);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }catch (NumberFormatException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid Id\"}");
        }

    }

    private CustomerDTO findById(String id) {
        for (CustomerDTO customerDTO : customerDTOList) {
            if (customerDTO.getId().equals(id)) {
                return customerDTO;
            }
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/company",
                    "root",
                    "Ijse@123");

            ResultSet resultSet = connection.prepareStatement("SELECT * FROM customer").executeQuery();
            JsonArrayBuilder allCustomer = Json.createArrayBuilder();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);

                JsonObjectBuilder customer = Json.createObjectBuilder();
                 customer.add("id", id);
                 customer.add("name", name);
                 customer.add("address", address);
                 allCustomer.add(customer.build());

                System.out.println("ID: " + id + "| Name: " + name + "| Address: " + address);
                CustomerDTO customerDTO = new CustomerDTO(id, name, address);

                customerDTOList.add(customerDTO);
            }
            StringBuilder customerJsonList = new StringBuilder("[");
            for (CustomerDTO customerDTO : customerDTOList) {

                String customerJson = String.format("{\"id\":\"%s\", \"name\":\"%s\", \"address\":\"%s\"}",
                        customerDTO.getId(),
                        customerDTO.getName(),
                        customerDTO.getAddress());

                customerJsonList.append(customerJson);

                if (customerDTOList.indexOf(customerDTO) != customerDTOList.size()-1) {
                    customerJsonList.append(",");
                }
            }
            customerJsonList.append("]");
            String customerJson = customerJsonList.toString();
            PrintWriter out = resp.getWriter();
            out.println(customerJson);


        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);}
    }
}