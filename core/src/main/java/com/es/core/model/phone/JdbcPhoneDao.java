package com.es.core.model.phone;

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

    private final static String INSERT_INTO_PHONES = "insert into phones values(?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
            " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final static String INSERT_INTO_PHONE2COLOR = "insert into phone2color (phoneId, colorId) values (?, ?)";


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
