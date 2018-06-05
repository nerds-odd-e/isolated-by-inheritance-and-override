package com.odde.isolated;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

public class OrderService {

    private String filePath = "C:\\temp\\testOrders.csv";

    public void syncBookOrders()
    {
        List<Order> orders = this.GetOrders();

        // only get orders of book
        List<Order> ordersOfBook = orders.stream().filter(x -> x.getType().equals("Book")).collect(toList());

        BookDao bookDao = new BookDao();
        for (Order order : ordersOfBook)
        {
            bookDao.insert(order);
        }
    }

    private List<Order> GetOrders()
    {
        // parse csv file to get orders
        List<Order> result = new ArrayList<>();

        // directly depend on File I/O
        try (Scanner scanner = new Scanner(filePath))
        {
            int rowCount = 0;

            while (scanner.hasNextLine()) {

                rowCount++;

                String content = scanner.nextLine();

                // Skip CSV header line
                if (rowCount > 1) {
                    String[] line = content.trim().split(",");

                    result.add(this.Mapping(line));
                }
            }
        }

        return result;
    }

    private Order Mapping(String[] line)
    {
        Order result = new Order(){{
            setProductName(line[0]);
            setType(line[1]);
            setPrice(Integer.parseInt(line[2]));
            setCustomerName(line[3]);
        }};

        return result;
    }
}
