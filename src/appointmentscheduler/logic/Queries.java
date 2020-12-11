/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appointmentscheduler.logic;

/**
 * Container class for SQL queries
 * @author martinarroyo
 */
public class Queries {
    // Report 1 @params month, date
    public static final String APPTYPES_BY_MONTH = "SELECT YEAR(start) AS year,MONTH(start) AS month,type,COUNT(type) AS number FROM appointment GROUP BY type,start HAVING month=? AND year=?;";
    // Report 2 @params userId
    public static final String APPTS_BY_CONSULTANT = "SELECT c.customerName,a.userId,a.type,a.description,a.location,date(a.start) AS meetingDate,time(a.start) AS startTime,time(a.end) AS endTime,\n" +
                                                            "c.customerID,c.address AS address1,c.address2,c.city,c.country,c.postalCode,c.phone FROM appointment a\n" +
                                                            "INNER JOIN (SELECT c.customerId,c.customerName,a.address,a.address2,city.city,country.country,a.postalCode,a.phone FROM customer c	\n" +
                                                            "INNER JOIN (SELECT addressID,address,address2,cityId,postalCode,phone FROM address) a ON c.addressId=a.addressId  AND c.active=1 \n" +
                                                            "INNER JOIN (SELECT cityId,city,countryId FROM city) city ON a.cityId=city.cityId\n" +
                                                            "INNER JOIN (SELECT countryId,country FROM country) country ON city.countryId=country.countryId)c ON a.customerId=c.customerId\n" +
                                                    "HAVING a.userId=?;";
    // Gets userId based on given Consultant name @param userName
    public static final String GET_CONSULTANTID_WNAME = "SELECT userId FROM user WHERE userName=?;";
    // Gets usernames from user table (Consultants)
    public static final String GET_CONSULTANT_NAMES = "SELECT userName FROM user;";
    // Gets username from user table based on ID
    public static final String GET_CONSULTANTNAME_WID = "SELECT userName FROM user WHERE userId=?;";
    // Report 3 @params customerName
    public static final String APPTS_BY_CLIENT = "SELECT c.customerName,a.userId,u.userName AS consultant,a.type,a.description,a.location,date(a.start) AS meetingDate,time(a.start) AS startTime,time(a.end) AS endTime,\n" +
                                                            "c.customerID,c.address AS address1,c.address2,c.city,c.country,c.postalCode,c.phone FROM appointment a\n" +
                                                            "INNER JOIN (SELECT c.customerId,c.customerName,a.address,a.address2,city.city,country.country,a.postalCode,a.phone FROM customer c	\n" +
                                                            "INNER JOIN (SELECT addressID,address,address2,cityId,postalCode,phone FROM address) a ON c.addressId=a.addressId  AND c.active=1 \n" +
                                                            "INNER JOIN (SELECT cityId,city,countryId FROM city) city ON a.cityId=city.cityId\n" +
                                                            "INNER JOIN (SELECT countryId,country FROM country) country ON city.countryId=country.countryId)c ON a.customerId=c.customerId\n" +
                                                            "INNER JOIN user u ON a.userId=u.userId\n" +
                                                    "HAVING c.customerName=?;";
    // Inserts client into the database
    public static final String INSERT_CLIENT = "INSERT INTO customer (customerName,addressId,active,createdBy,lastUpdateBy,createDate) VALUES (?,?,?,?,?,NOW())";
    // Gets the client id
    public static final String GET_CLIENTID = "SELECT customerID FROM customer WHERE LCASE(customername)=? AND addressId=?";
    // checks if client exists
    public static final String CLIENT_EXISTS = "SELECT customerID FROM customer WHERE LCASE(customerName)=? AND addressID=? AND active=1";
    // modifies the client
    public static final String MODIFY_CLIENT = "UPDATE customer SET customerName=?, addressId=?, createdBy=?, lastUpdateBy=? WHERE customerId=?";
    // Soft deletes client in database
    public static final String DELETE_CLIENT = "UPDATE customer SET active=0 WHERE customerId=?;";
    // Gets active client list from database
    public static final String GET_ALLCLIENTS = "SELECT c.customerId,c.customerName,c.addressId,a.address,a.address2,city.city,country.country,\n" +
                                 "a.postalCode,a.phone\n" +
                                "FROM customer c	\n" +
                                "INNER JOIN (SELECT addressID,address,address2,cityId,postalCode,phone FROM address) a\n" +
                                "ON c.addressId=a.addressId  AND c.active=1\n" +
                                "INNER JOIN (SELECT cityId,city,countryId FROM city) city\n" +
                                "ON a.cityId=city.cityId\n" +
                                "INNER JOIN (SELECT countryId,country FROM country) country\n" +
                                "ON city.countryId=country.countryId";
    // Gets appointment id
    public static final String GET_APPID = "SELECT appointmentId FROM appointment WHERE userId=? AND start=? AND end=?";
    // Gets all appointments in the week
    public static final String GET_APP_BYWEEK = "SELECT c.customerName,a.type,a.description,a.location,date(a.start) AS meetingDate,time(a.start) AS startTime,time(a.end) AS endTime,\n" +
                                        "c.customerID,c.address AS address1,c.address2,c.city,c.country,c.postalCode,c.phone FROM appointment a\n" +
                                "	INNER JOIN (SELECT c.customerId,c.customerName,a.address,a.address2,city.city,country.country,a.postalCode,a.phone FROM customer c	\n" +
                                "	INNER JOIN (SELECT addressID,address,address2,cityId,postalCode,phone FROM address) a ON c.addressId=a.addressId  AND c.active=1 \n" +
                                "	INNER JOIN (SELECT cityId,city,countryId FROM city) city ON a.cityId=city.cityId\n" +
                                "	INNER JOIN (SELECT countryId,country FROM country) country ON city.countryId=country.countryId)c ON a.customerId=c.customerId\n" +
                                       "HAVING meetingDate BETWEEN ? and date_add(?, INTERVAL 1 WEEK) ORDER BY meetingDate,startTime;";
    // Gets all appointments in the month
    public static final String GET_APP_BYMONTH = "SELECT c.customerName,a.type,a.description,a.location,date(a.start) AS meetingDate,time(a.start) AS startTime,time(a.end) AS endTime,\n" +
                                            "c.customerID,c.address AS address1,c.address2,c.city,c.country,c.postalCode,c.phone FROM appointment a\n" +
                                            "	INNER JOIN (SELECT c.customerId,c.customerName,a.address,a.address2,city.city,country.country,a.postalCode,a.phone FROM customer c	\n" +
                                        "	INNER JOIN (SELECT addressID,address,address2,cityId,postalCode,phone FROM address) a ON c.addressId=a.addressId  AND c.active=1 \n" +
                                        "	INNER JOIN (SELECT cityId,city,countryId FROM city) city ON a.cityId=city.cityId\n" +
                                        "	INNER JOIN (SELECT countryId,country FROM country) country ON city.countryId=country.countryId)c ON a.customerId=c.customerId											\n" +
                                        "HAVING meetingDate BETWEEN convert(concat(?,'-',?,'-','01'),date) AND date_add(convert(concat(?,'-',?,'-','01'),date), INTERVAL 1 MONTH)\n" +
                                        "ORDER BY meetingDate, startTime;";
    // Adds an appointment to the database
    public static final String ADD_APPT = "INSERT INTO appointment (customerId,userId,description,location,type,start,end,createDate,createdBy,lastUpdate,lastUpdateBy, url, title,contact) "
                                        + "VALUES (?,?,?,?,?,?,?,TIMESTAMP(curdate(),curtime()),?,TIMESTAMP(curdate(),curtime()),?,'','','');";
    // Updates an appointment in the database
    public static final String UPDATE_APPT = "UPDATE appointment SET customerId=?, description=?, "
                                        + "location=?, type=?, start=?, "
                                        + "end=?, lastUpdate=TIMESTAMP(curdate(),curtime()), "
                                        + "lastUpdateBy=? WHERE appointmentId=?;";
    // Deletes an appointment in the database
    public static final String DELETE_APPT = "DELETE FROM appointment WHERE appointmentId=?";
    // Checks if there is a scheduling conflict in the database when creating a new appointment
    public static final String SCHEDULE_CONFLICT = "SELECT appointmentId FROM appointment WHERE (start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?);";
    // Checks if there is a scheduling conflict when updating an existing appointment
    public static final String SCHEDULE_CONFLICT_UPDT = "SELECT appointmentId FROM appointment WHERE ((start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?)) AND appointmentId <> ?;";
    // Gets the address ID
    public static final String GET_ADDRID = "SELECT addressID FROM address WHERE LCASE(address)=? AND LCASE(address2)=IFNULL(?,'') AND postalCode=? AND phone=? AND cityId=?;";
    // Checks if address exists in database
    public static final String ADDR_EXISTS = "SELECT addressID FROM address WHERE LCASE(address)=? AND LCASE(address2)=IFNULL(?,'') AND postalCode=? AND phone=? AND cityId=?;";
    // Adds address to database
    public static final String ADD_ADDRESS = "INSERT INTO address (address, address2, cityId, postalCode, phone,createdBy,lastUpdateBy,createDate) VALUES (?,?,?,?,?,?,?,NOW());";
    // Determines if user is authenticated
    public static final String IS_AUTH = "SELECT userId, userName,password FROM user WHERE userName=? and password=?;";
    // Gets the city id
    public static final String GET_CITYID = "SELECT cityID FROM city WHERE LCASE(city)=? AND countryId=?;";
    // Checks if a city exists in the database
    public static final String CITY_EXISTS = "SELECT cityID FROM city WHERE LOWER(city)=? AND countryId=?;";
    // Adds a city to the database
    public static final String ADD_CITY = "INSERT INTO city (city,countryID,createDate,createdBy,lastUpdateBy) VALUES (?,?,NOW(),?,?);";
    // Gets the country id
    public static final String GET_CTRYID = "SELECT countryID FROM country WHERE LCASE(country)=?;";
    // Checks if country exists in database
    public static final String CTRY_EXISTS = "SELECT countryID FROM country WHERE LOWER(country)=? LIMIT 1";
    // Adds country to database
    public static final String ADD_CTRY = "INSERT INTO country (country,createDate,createdBy,lastUpdateBy) VALUES (?,NOW(),?,?)";
    // Checks if there is an appointment within 15 minutes of the current time (this will only be run at login time)
    public static final String APPT_IN_15 = "SELECT appointmentId,userId, TIMESTAMP(curdate(),curtime()) AS logTime,start, TIMESTAMPDIFF(MINUTE,TIMESTAMP(curdate(),curtime()),start) AS diff  FROM appointment HAVING TIMESTAMPDIFF(MINUTE,TIMESTAMP(curdate(),curtime()),start) BETWEEN 0 AND 15 AND  userID=?;";
    // Updates the current address
    public static final String UPDATE_ADDR = "UPDATE address\n" +
                                             "SET address=?,address2=?,cityId=?,postalCode=?, phone=?,  lastUpdateBy=?, lastUpdate=TIMESTAMP(curdate(),curtime())\n" +
                                             "WHERE addressId=?";
    private Queries(){};
    /*
    public static final String GET_APP_BYMONTH = "SELECT c.customerName,a.type,a.description,a.location,date(a.start) AS meetingDate,time(a.start) AS startTime,time(a.end) AS endTime,\n" +
                                            "c.customerID,c.address AS address1,c.address2,c.city,c.country,c.postalCode,c.phone FROM appointment a\n" +
                                            "	INNER JOIN (SELECT c.customerId,c.customerName,a.address,a.address2,city.city,country.country,a.postalCode,a.phone FROM customer c	\n" +
                                        "	INNER JOIN (SELECT addressID,address,address2,cityId,postalCode,phone FROM address) a ON c.addressId=a.addressId  AND c.active=1 \n" +
                                        "	INNER JOIN (SELECT cityId,city,countryId FROM city) city ON a.cityId=city.cityId\n" +
                                        "	INNER JOIN (SELECT countryId,country FROM country) country ON city.countryId=country.countryId)c ON a.customerId=c.customerId											\n" +
                                        "HAVING meetingDate BETWEEN convert(concat(?,'-',?,'-','01'),date) AND date_add(convert(concat(?,'-',?,'-','01'),date), INTERVAL 1 MONTH)\n" +
                                        "ORDER BY meetingDate, startTime;";
    */
}
