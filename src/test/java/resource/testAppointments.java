package resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.ApiTestUtils;

import static spark.Spark.awaitInitialization;
import static spark.Spark.stop;

public class testAppointments {

    @Before
    public void setup() throws Exception {
        Appointments.establish();

        awaitInitialization();
    }

    @After
    public void tearDown() throws Exception {
        stop();
    }

    @Test
    public void testObjectsGET() {
        String testUrl = "/appointments/123456/2018-07-22/free";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertEquals("{slots:[\"10:00\"]}", res.body);
    }

    @Test
    public void testObjectsGETEmpty() {
        String testUrl = "/appointments/1234567/2018-07-22/free";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertEquals("{slots:[]}", res.body);
    }

    @Test
    public void testObjectsPOST() {
        String testUrl = "/appointments/123456/2018-07-22/10:30/MaDongMei";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertTrue(res.body.contains("appointmentId:"));
    }

    @Test
    public void testObjectsPOSTWrongName() {
        String testUrl = "/appointments/123456/2018-07-22/10:30/MaDongMe";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertTrue(res.body.contains("Unable to reserve the appointment"));
    }

    @Test
    public void testObjectsPOSTSameTime() {
        String testUrl = "/appointments/123456/2018-07-22/10:00/MaDongMei";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertTrue(res.body.contains("Unable to reserve the appointment"));
    }

    @Test
    public void testObjectsDelete() {
        String testUrl = "/appointments/123456/2018-07-22/10:30/MaDongMei";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        Assert.assertEquals(200, res.status);
        String[] pieces = res.body.split(":");
        String appointId = pieces[1].substring(0, pieces[1].length() - 1);

        testUrl = "/appointments/123456/" + appointId;
        res = ApiTestUtils.request("DELETE", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertTrue(res.body.contains("success"));
    }

    @Test
    public void testObjectsDeleteWrongTokenId() {
        String testUrl = "/appointments/123456/2018-07-22/10:30/MaDongMei";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        Assert.assertEquals(200, res.status);
        String[] pieces = res.body.split(":");
        String appointId = pieces[1].substring(0, pieces[1].length() - 1);

        testUrl = "/appointments/123457/" + appointId;
        res = ApiTestUtils.request("DELETE", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertTrue(res.body.contains("Unable to cancel the appointment"));
    }

    @Test
    public void testObjectsDeleteWrongAppointmentId() {
        String testUrl = "/appointments/123456/2018-07-22/10:30/MaDongMei";

        ApiTestUtils.TestResponse res = ApiTestUtils.request("POST", testUrl, null);
        Assert.assertEquals(200, res.status);

        testUrl = "/appointments/123456/12345678";
        res = ApiTestUtils.request("DELETE", testUrl, null);
        Assert.assertEquals(200, res.status);
        Assert.assertTrue(res.body.contains("Unable to cancel the appointment"));
    }
}
