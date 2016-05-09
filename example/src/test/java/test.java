import net.fengni.example.dao.ProductDao;
import net.fengni.example.dao.UserDao;
import net.fengni.example.model.Product;
import net.fengni.example.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fengni on 2015/10/16.
 */
public class test {
    public static ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    UserDao userDao = (UserDao)context.getBean("userDao");
    ProductDao productDao = (ProductDao)context.getBean("productDao");
    @Test
    public void testSaveUser(){  //User按照ID分表了
        User user = new User();
        user.setId(123456);
        user.setGrade(1);
        user.setPassword("11111");
        user.setUsername("11111");
        user.setPhoneNo("010-65451521");
        Assert.assertTrue(userDao.insert(user) == 1);
    }

    @Test
    public void testSaveProduct(){//Product没有分表
        Product product = new Product();
        product.setName("apple");
        product.setDesc("soup apple");
        Assert.assertTrue(productDao.insert(product) == 1);
        Assert.assertTrue(product.getId() > 0);
        System.out.println(product.getId());
    }
}
