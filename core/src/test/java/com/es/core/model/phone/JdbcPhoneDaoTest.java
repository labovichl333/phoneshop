package com.es.core.model.phone;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context/test-applicationContext-core.xml")
public class JdbcPhoneDaoTest {
    private static final long PHONE_ID_TO_GET = 1000L;
    private static final long PHONE_ID_TO_SAVE = 9999L;
    private static final String PHONE_NEW_MODEL_TO_SAVE = "new Model";
    private static final String PHONE_NEW_BRAND_TO_SAVE = "new Brand";
    private static final long FIRST_COLOR_ID_TO_SAVE = 1000L;
    private static final long SECOND_COLOR_ID_TO_SAVE = 1001L;
    private static final String FIRST_COLOR_CODE_TO_SAVE = "Black";
    private static final String SECOND_COLOR_CODE_TO_SAVE = "White";
    @Resource
    private PhoneDao phoneDao;

    @Test
    public void shouldCorrectlySavePhone() {
        Optional<Phone> optionalPhone = phoneDao.get(PHONE_ID_TO_GET);
        Assert.assertTrue(optionalPhone.isPresent());
        Phone phone = optionalPhone.get();
        Color color1 = new Color(FIRST_COLOR_ID_TO_SAVE, FIRST_COLOR_CODE_TO_SAVE);
        Color color2 = new Color(SECOND_COLOR_ID_TO_SAVE, SECOND_COLOR_CODE_TO_SAVE);
        Set<Color> colors = new HashSet<>();
        colors.add(color1);
        colors.add(color2);
        phone.setId(PHONE_ID_TO_SAVE);
        phone.setModel(PHONE_NEW_MODEL_TO_SAVE);
        phone.setBrand(PHONE_NEW_BRAND_TO_SAVE);
        phone.setColors(colors);
        phoneDao.save(phone);
        Optional<Phone> newOptionalPhone = phoneDao.get(PHONE_ID_TO_SAVE);
        Assert.assertTrue(newOptionalPhone.isPresent());
        Phone newPhone = newOptionalPhone.get();
        Assert.assertEquals(phone.getDescription(), newPhone.getDescription());
        Assert.assertEquals(phone.getDeviceType(), newPhone.getDeviceType());
        Assert.assertEquals(phone.getBluetooth(), newPhone.getBluetooth());
        Assert.assertEquals(phone.getColors().size(), newPhone.getColors().size());
        Assert.assertEquals(phone.getColors(), newPhone.getColors());
    }
}
