/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.logic;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author martinarroyo
 */
public class AddressTest {
    // TODO: finish adding tests
    public AddressTest() {
    }
    /**
     * Test of getAddressID method, of class Address.
     */
    @Test
    public void testGetAddressID1() {
        System.out.println("Test that function returns -1 when address is not found or invalid");
        String addr1 = "";
        String addr2 = "";
        String zip = "";
        String phone = "";
        int cityID = 0;
        int expResult = -1;
        int result = Address.getAddressID(addr1, addr2, zip, phone, cityID);
        assertEquals(expResult, result);
    }
    @Test
    public void testGetAddressID2() {
        System.out.println("Test that function retrieves known address.");
        int countryID = new Country().getCountryID("USA");
        int cityID = new City().getCityID("Staten Island", countryID);
        String addr1 = "111 new street";
        String addr2 = "";
        String zip = "10303";
        String phone = "123-345-2222";
        int expResult = 19;
        int result = Address.getAddressID(addr1, addr2, zip, phone, cityID);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of exists method, of class Address.
     */
    @Test
    public void testExists1() {
        System.out.println("Tests that function returns false if given an address which is not in the database.");
        String addr1 = "";
        String addr2 = "";
        String zip = "";
        String phone = "";
        int cityId = 0;
        boolean expResult = false;
        boolean result = Address.exists(addr1, addr2, zip, phone, cityId);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testExists2() {
        System.out.println("Tests that function returns true for existing address");
        int countryID = new Country().getCountryID("USA");
        int cityID = new City().getCityID("Staten Island", countryID);
        String addr1 = "111 new street";
        String addr2 = "";
        String zip = "10303";
        String phone = "123-345-2222";
        boolean expResult = true;
        boolean result = Address.exists(addr1, addr2, zip, phone, cityID);
        assertEquals(expResult, result);
    }
    /**
     * Test of addAddress method, of class Address.
     * @throws java.lang.Exception
     */ 
    @Test
    public void testAddAddress1() throws Exception{
        System.out.println("addAddress");
        Address addr = null;
        int cityID = 0;
       // assertThrows(null, NullPointerException.class,Address.addAddress(addr, cityID));
        //assertFalse(Address.exists(addr.getAddress1(), addr.getAddress2(),
                           //         addr.getZipcode(),addr.getPhone(), cityID)); 
        
    }

    /**
     * Test of updateAddress method, of class Address.
     */
    @Test
    public void testUpdateAddress() {
        System.out.println("updateAddress");
        Address addr = null;
        int cityID = 0;
        //Address.updateAddress(addr, cityID);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
