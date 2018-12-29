import com.jsj.member.ob.utils.StringUtils;
import org.junit.Test;

public class stringUtilsTest {

    @Test
    public void testIdCard() {

        String idNumber = "23210119810209382X";
        boolean isRight = StringUtils.isIdNumber(idNumber);

        System.out.println(isRight);

    }

}