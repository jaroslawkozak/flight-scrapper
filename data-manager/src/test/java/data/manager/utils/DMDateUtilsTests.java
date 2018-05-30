package data.manager.utils;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class DMDateUtilsTests {
  
    @Parameter(value = 0)
    public String fromDate;

    @Parameter(value = 1)
    public String expectedDate;
    
    @Parameters(name = "{index}: testAddMonth {0} => {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"2018-03-03", "2018-04-03"},
                {"2018-01-30", "2018-02-28"},
                {"2018-05-31", "2018-06-30"},
                {"2018-12-15", "2019-01-15"}
        });
    }
    
    @Test
    public void testAddMonth() {
        String actual = "";
        try {
            actual = DMDateUtils.addMonth(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        Assert.assertThat("Dates should be equal", actual, CoreMatchers.is(expectedDate));
    }
}
