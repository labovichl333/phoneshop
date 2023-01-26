package com.es.core.model.phone;

import com.es.core.util.StringUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private final static String SELECT_PHONE_BY_ID = "select * from phones where id=?";

    private final static String SELECT_PHONES_BY_OFFSET_AND_LIMIT = "select * from phones offset ? limit ?";

    private final static String SELECT_COLOR_BY_ID = "select * from colors where id=?";

    private final static String SELECT_COLOR_ID_BY_PHONE_ID = "select colorId from phone2color where phoneId=?";

    private final static String SELECT_PHONE_QUANTITY_IN_STOCK_BY_ID = "select stock from stocks where phoneId=?";

    private final static String INSERT_INTO_PHONES = "insert into phones (id, brand, model, price, displaySizeInches," +
            " weightGr, lengthMm, widthMm, heightMm, announced, deviceType, os, displayResolution, pixelDensity, " +
            "displayTechnology, backCameraMegapixels, frontCameraMegapixels, ramGb, internalStorageGb," +
            " batteryCapacityMah, talkTimeHours, standByTimeHours, bluetooth, positioning, imageUrl," +
            " description) values(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String INSERT_INTO_PHONE2COLOR = "insert into phone2color (phoneId, colorId) values (?, ?)";

    private static final String SELECT_ALL_IN_STOCK_AND_NOT_NULL_PRICE = "select * from phones, stocks" +
            " where phones.id = stocks.phoneId and stocks.stock > 0 and phones.price is not null";
    private static final String SEARCH_PART = " and lower(phones.model) like '%";
    private static final String SORT_PART = " order by ";
    private static final String SELECT_IN_STOCK_PHONES_COUNT = "select count(*) from phones, stocks" +
            " where phones.id = stocks.phoneId and stocks.stock > 0 and phones.price is not null";
    private static final String LIMIT_PART = " limit ";
    private static final String OFFSET_PART = " offset ";


    @Resource
    private JdbcTemplate jdbcTemplate;


    public Optional<Phone> get(final Long key) {
        Optional<Phone> phone = Optional.ofNullable(
                jdbcTemplate.queryForObject(SELECT_PHONE_BY_ID,
                        BeanPropertyRowMapper.newInstance(Phone.class), key));
        phone.ifPresent(this::setPhoneColors);
        return phone;
    }

    public void save(final Phone phone) {
        jdbcTemplate.update(INSERT_INTO_PHONES,
                phone.getId(), phone.getBrand(), phone.getModel(), phone.getPrice(), phone.getDisplaySizeInches(),
                phone.getWeightGr(), phone.getLengthMm(), phone.getWidthMm(), phone.getHeightMm(),
                phone.getAnnounced(), phone.getDeviceType(), phone.getOs(), phone.getDisplayResolution(),
                phone.getPixelDensity(), phone.getDisplayTechnology(), phone.getBackCameraMegapixels(),
                phone.getFrontCameraMegapixels(), phone.getRamGb(), phone.getInternalStorageGb(),
                phone.getBatteryCapacityMah(), phone.getTalkTimeHours(), phone.getStandByTimeHours(),
                phone.getBluetooth(), phone.getPositioning(), phone.getImageUrl(), phone.getDescription());

        jdbcTemplate.batchUpdate(INSERT_INTO_PHONE2COLOR,
                phone.getColors(), phone.getColors().size(), (preparedStatement, color) -> {
                    preparedStatement.setLong(1, phone.getId());
                    preparedStatement.setLong(2, color.getId());
                });
    }

    public List<Phone> findAll(int offset, int limit) {
        List<Phone> phones = jdbcTemplate.query(SELECT_PHONES_BY_OFFSET_AND_LIMIT,
                new BeanPropertyRowMapper(Phone.class), offset, limit);
        phones.forEach(this::setPhoneColors);
        return phones;
    }

    @Override
    public List<Phone> findPhonesInStock(String query, String sortField, String sortOrder, int offset, int limit) {

        StringBuilder sqlQuery = new StringBuilder(SELECT_ALL_IN_STOCK_AND_NOT_NULL_PRICE);
        if (!StringUtil.isBlank(query)) {
            sqlQuery.append(SEARCH_PART).append(query.trim().toLowerCase()).append("%'");
        }
        if (!StringUtil.isBlank(sortField)) {
            sqlQuery.append(SORT_PART).append(sortField).append(" ").append(sortOrder);
        }
        sqlQuery.append(LIMIT_PART).append(limit).append(OFFSET_PART).append(offset);


        List<Phone> phonesInStock = jdbcTemplate.query(sqlQuery.toString(), new BeanPropertyRowMapper<>(Phone.class));

        phonesInStock.forEach(this::setPhoneColors);
        return phonesInStock;
    }

    @Override
    public long getQuantityOfPhonesInStock(String query) {
        StringBuilder sqlQuery = new StringBuilder(SELECT_IN_STOCK_PHONES_COUNT);
        if (!StringUtil.isBlank(query)) {
            sqlQuery.append(SEARCH_PART).append(query.trim().toLowerCase()).append("%'");
        }
        return jdbcTemplate.queryForObject(sqlQuery.toString(), Long.class);
    }

    @Override
    public int getInStockQuantity(long phoneId) {
        return jdbcTemplate.queryForObject(SELECT_PHONE_QUANTITY_IN_STOCK_BY_ID, Integer.class,phoneId);
    }

    private void setPhoneColors(Phone phone) {
        List<Long> colorIds = jdbcTemplate.queryForList(SELECT_COLOR_ID_BY_PHONE_ID,
                Long.class, phone.getId());
        Set<Color> colors = colorIds.stream()
                .map(colorId -> jdbcTemplate.queryForObject(SELECT_COLOR_BY_ID,
                        BeanPropertyRowMapper.newInstance(Color.class),
                        colorId))
                .collect(Collectors.toSet());
        phone.setColors(colors);
    }

}
