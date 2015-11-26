package SpringTest;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ncu.testbank.admin.dao.IAcademyDao;
import com.ncu.testbank.admin.data.Academy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:springTest.xml"})
public class SpringJunitTest {
	
	@Autowired
	private IAcademyDao dataDao;
	
	@Test
	public void test() {
		Academy academy = new Academy();
		academy.setAcademy_id("1");
		academy.setName("demo");
		int a = dataDao.updateOne(academy);
		System.out.println(a);
	}
}
