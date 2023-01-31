package com.es.core.model.order;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.JdbcPhoneDao;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JdbcOrderDao implements OrderDao {

    private final String SELECT_ORDER_BY_ID = "select * from orders where id=?";
    private final String SELECT_ORDER_BY_SECURE_ID = "select * from orders where secureId=?";

    private final String SELECT_ORDER_ITEMS_BY_ORDER_ID = "select * from orderItems where orderId=?";

    private static final String INSERT_INTO_ORDERS = "insert into orders (secureId, subtotal, deliveryPrice, totalPrice," +
            "firstName, lastName, deliveryAddress, contactPhoneNo, additionalInformation, status) " +
            "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_INTO_ORDER_ITEMS = "insert into orderItems (phoneId, orderId, quantity)" +
            " values (?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;

    private final JdbcPhoneDao jdbcPhoneDao;

    @Override
    public Optional<Order> findById(Long id) {
        Optional<Order> order = Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ORDER_BY_ID,
                BeanPropertyRowMapper.newInstance(Order.class), id));
        order.ifPresent(this::setOrderItems);
        return order;
    }

    @Override
    public Optional<Order> findBySecureId(String secureId) {
        Optional<Order> order = Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_ORDER_BY_SECURE_ID,
                        BeanPropertyRowMapper.newInstance(Order.class), secureId));
        order.ifPresent(this::setOrderItems);
        return order;
    }

    @Override
    public void save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(INSERT_INTO_ORDERS);
            ps.setString(1, order.getSecureId());
            ps.setObject(2, order.getSubtotal());
            ps.setObject(3, order.getDeliveryPrice());
            ps.setObject(4, order.getTotalPrice());
            ps.setString(5, order.getFirstName());
            ps.setString(6, order.getLastName());
            ps.setString(7, order.getDeliveryAddress());
            ps.setString(8, order.getContactPhoneNo());
            ps.setString(9, order.getAdditionalInformation());
            ps.setString(10, order.getStatus().toString());
            return ps;
        },keyHolder);

        order.setId(keyHolder.getKey().longValue());

        jdbcTemplate.batchUpdate(INSERT_INTO_ORDER_ITEMS,order.getOrderItems(),order.getOrderItems().size(),
                (preparedStatement, orderItem) ->{
                    preparedStatement.setLong(1, orderItem.getPhone().getId());
                    preparedStatement.setLong(2, orderItem.getOrder().getId());
                    preparedStatement.setLong(3, orderItem.getQuantity());
                });
        order.getOrderItems().forEach(orderItem -> jdbcPhoneDao.decreaseStockQuantity(orderItem.getPhone().getId(),
                orderItem.getQuantity()));
    }

    private void setOrderItems(Order order){
        order.setOrderItems(jdbcTemplate.query(SELECT_ORDER_ITEMS_BY_ORDER_ID
                , new Object[]{order.getId()},(resultSet, i) -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(resultSet.getLong(1));
                    orderItem.setPhone(jdbcPhoneDao.get(resultSet.getLong(2))
                            .orElseThrow(PhoneNotFoundException::new));
                    orderItem.setOrder(order);
                    orderItem.setQuantity(resultSet.getLong(4));
                    return orderItem;
                }));
    }
}







