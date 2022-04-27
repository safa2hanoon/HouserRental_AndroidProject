package edu.berziet.houserental.dbhelpers;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.berziet.houserental.models.CityModel;
import edu.berziet.houserental.models.CountryModel;
import edu.berziet.houserental.models.NationalityModel;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.PropertyModel;

public class RentalSqliteOpenHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "rentalDatabase";
    private static final int DATABASE_VERSION = 1;


    // Table Names
    private static final String TABLE_PROPERTIES = "properties";
    private static final String TABLE_COUNTRY = "countries";
    private static final String TABLE_CITY = "cities";
    private static final String TABLE_NATIONALITY = "nationalities";

    // Property Table Columns
    private static final String PROPERTY_COLUMN_ID = "property_id";
    private static final String PROPERTY_COLUMN_ADVERTISE_DATE = "advertise_date";
    private static final String PROPERTY_COLUMN_AVAILABILITY_DATE = "availability_date";
    private static final String PROPERTY_COLUMN_BEDROOMS_COUNT = "bedrooms_count";
    private static final String PROPERTY_COLUMN_CONSTRUCTION_YEAR = "construction_year";
    private static final String PROPERTY_COLUMN_DESCRIPTION = "description";
    private static final String PROPERTY_COLUMN_HAS_BALCONY = "has_balcony";
    private static final String PROPERTY_COLUMN_HAS_GARDEN = "has_garden";
    private static final String PROPERTY_COLUMN_RENTAL_PRICE = "rental_price";
    private static final String PROPERTY_COLUMN_RENTED = "rented";
    private static final String PROPERTY_COLUMN_STATUS = "status";
    private static final String PROPERTY_COLUMN_SURFACE_AREA = "surface_area";
    private static final String PROPERTY_COLUMN_CITY_ID = "city_id";
    private static final String PROPERTY_COLUMN_AGENCY_ID = "agency_id";

    // Country Table Columns
    private static final String COUNTRY_COLUMN_ID = "country_id";
    private static final String COUNTRY_COLUMN_NAME = "name";
    private static final String COUNTRY_COLUMN_ZIP_CODE = "zip_code";

    // City Table Columns
    private static final String CITY_COLUMN_ID = "city_id";
    private static final String CITY_COLUMN_NAME = "name";
    private static final String CITY_COLUMN_COUNTRY_ID = "country_id";

    // Nationality Table Columns
    private static final String NATIONALITY_COLUMN_ID = "nationality_id";
    private static final String NATIONALITY_COLUMN_NATIONALITY = "nationality";

    public RentalSqliteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create country table
        String CREATE_COUNTRY_TABLE = "CREATE TABLE "+ TABLE_COUNTRY+
                "("+
                COUNTRY_COLUMN_ID + " INTEGER PRIMARY KEY," +
                COUNTRY_COLUMN_NAME + " TEXT," +
                COUNTRY_COLUMN_ZIP_CODE + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_COUNTRY_TABLE);

        // create city table
        String CREATE_CITY_TABLE = "CREATE TABLE "+ TABLE_CITY+
                "("+
                CITY_COLUMN_ID + " INTEGER PRIMARY KEY," +
                CITY_COLUMN_NAME + " TEXT," +
                CITY_COLUMN_COUNTRY_ID + " INTEGER "+
                ")";
        sqLiteDatabase.execSQL(CREATE_CITY_TABLE);

        //create property table
        String CREATE_PROPERTY_TABLE = "CREATE TABLE " + TABLE_PROPERTIES +
                "(" +
                PROPERTY_COLUMN_ID + " INTEGER PRIMARY KEY," +
                PROPERTY_COLUMN_ADVERTISE_DATE + " TEXT," +
                PROPERTY_COLUMN_AVAILABILITY_DATE + " TEXT," +
                PROPERTY_COLUMN_BEDROOMS_COUNT + " INTEGER," +
                PROPERTY_COLUMN_CONSTRUCTION_YEAR + " INTEGER," +
                PROPERTY_COLUMN_DESCRIPTION + " TEXT," +
                PROPERTY_COLUMN_HAS_BALCONY + " TEXT," +
                PROPERTY_COLUMN_HAS_GARDEN + " TEXT," +
                PROPERTY_COLUMN_RENTAL_PRICE + " INTEGER," +
                PROPERTY_COLUMN_RENTED + " TEXT," +
                PROPERTY_COLUMN_STATUS + " TEXT," +
                PROPERTY_COLUMN_SURFACE_AREA + " INTEGER," +
                PROPERTY_COLUMN_CITY_ID + " INTEGER ," +
                PROPERTY_COLUMN_AGENCY_ID + " TEXT" +
                ")";

        sqLiteDatabase.execSQL(CREATE_PROPERTY_TABLE);

        // create nationality table
        String CREATE_NATIONALITY_TABLE = "CREATE TABLE " + TABLE_NATIONALITY +
                "(" +
                NATIONALITY_COLUMN_ID + " INTEGER PRIMARY KEY," +
                NATIONALITY_COLUMN_NATIONALITY + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(CREATE_NATIONALITY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addCountry(CountryModel country){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COUNTRY_COLUMN_ID, country.getCountryId());
        contentValues.put(COUNTRY_COLUMN_NAME, country.getName());
        contentValues.put(COUNTRY_COLUMN_ZIP_CODE, country.getZipCode());
        sqLiteDatabase.insert(TABLE_COUNTRY,null,contentValues);
        sqLiteDatabase.close();
    }
    @SuppressLint("Range")
    public List<CountryModel> getCountriesList(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_COUNTRY,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<CountryModel> countriesList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                CountryModel country = new CountryModel();
                country.setCountryId(cursor.getInt(cursor.getColumnIndex(COUNTRY_COLUMN_ID)));
                country.setName(cursor.getString(cursor.getColumnIndex(COUNTRY_COLUMN_NAME)));
                country.setZipCode(cursor.getString(cursor.getColumnIndex(COUNTRY_COLUMN_ZIP_CODE)));
                countriesList.add(country);
            } while (cursor.moveToNext());
        }
        return countriesList;
    }

    public void insertNationality(NationalityModel nationality){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NATIONALITY_COLUMN_ID, nationality.getNationalityId());
        contentValues.put(NATIONALITY_COLUMN_NATIONALITY, nationality.getNationality());
        sqLiteDatabase.insert(TABLE_NATIONALITY,null,contentValues);
        sqLiteDatabase.close();
    }
    public List<NationalityModel> getNationalityList(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_NATIONALITY,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<NationalityModel> nationalitiesList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                NationalityModel nationalityModel = new NationalityModel(
                        cursor.getInt(cursor.getColumnIndex(NATIONALITY_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(NATIONALITY_COLUMN_NATIONALITY))
                );
                nationalitiesList.add(nationalityModel);
            } while (cursor.moveToNext());
        }
        return nationalitiesList;
    }

    public void insertProperty(PropertyModel property){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROPERTY_COLUMN_ID, property.getId());
        contentValues.put(PROPERTY_COLUMN_ADVERTISE_DATE, property.getAdvertiseDate());
        contentValues.put(PROPERTY_COLUMN_AVAILABILITY_DATE, property.getAvailabilityDate());
        contentValues.put(PROPERTY_COLUMN_BEDROOMS_COUNT, property.getBedroomsCount());
        contentValues.put(PROPERTY_COLUMN_CONSTRUCTION_YEAR, property.getConstructionYear());
        contentValues.put(PROPERTY_COLUMN_DESCRIPTION, property.getDescription());
        contentValues.put(PROPERTY_COLUMN_HAS_BALCONY, property.isHasBalcony()?1:0);
        contentValues.put(PROPERTY_COLUMN_HAS_GARDEN, property.isHasGarden()?1:0);
        contentValues.put(PROPERTY_COLUMN_RENTAL_PRICE, property.getRentalPrice());
        contentValues.put(PROPERTY_COLUMN_RENTED, property.isRented()?1:0);
        contentValues.put(PROPERTY_COLUMN_STATUS, property.getStatus());
        contentValues.put(PROPERTY_COLUMN_SURFACE_AREA, property.getSurfaceArea());
        contentValues.put(PROPERTY_COLUMN_CITY_ID, property.getCityId());
        contentValues.put(PROPERTY_COLUMN_AGENCY_ID, property.getAgencyId());

        sqLiteDatabase.insert(TABLE_PROPERTIES,null,contentValues);
    }
    public void deleteAllProperties(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PROPERTIES,null,null);
    }
    public List<PropertyModel> getAllPropertiesList(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_PROPERTIES,
                new String[]{PROPERTY_COLUMN_ID,PROPERTY_COLUMN_SURFACE_AREA,PROPERTY_COLUMN_BEDROOMS_COUNT,
                        PROPERTY_COLUMN_RENTAL_PRICE,PROPERTY_COLUMN_STATUS,PROPERTY_COLUMN_HAS_BALCONY,
                        PROPERTY_COLUMN_HAS_GARDEN,PROPERTY_COLUMN_CONSTRUCTION_YEAR,PROPERTY_COLUMN_AVAILABILITY_DATE,
                        PROPERTY_COLUMN_DESCRIPTION,PROPERTY_COLUMN_CITY_ID,PROPERTY_COLUMN_AGENCY_ID,
                        PROPERTY_COLUMN_ADVERTISE_DATE},
                null,
                null,
                null,
                null,
                null
        );
        List<PropertyModel> propertiesList = new ArrayList<>();
        try {
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                PropertyModel propertyModel = new PropertyModel(
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_SURFACE_AREA)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_BEDROOMS_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_RENTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_STATUS)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_HAS_BALCONY))==1,
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_HAS_GARDEN))==1,
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_CONSTRUCTION_YEAR)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_AVAILABILITY_DATE)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_CITY_ID)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_AGENCY_ID)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_ADVERTISE_DATE)),
                        new ArrayList<>()
                );
                propertiesList.add(propertyModel);
            } while (cursor.moveToNext());
        }
        } catch (Exception e) {
            Log.d("MySqliteError", "Error while trying to get properties list from database");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return propertiesList;
    }
    public List<PropertyModel> getTopFivePropertiesList(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format(
                "select * from %s order by %s desc limit 5",
                TABLE_PROPERTIES,
                PROPERTY_COLUMN_ADVERTISE_DATE
        );
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        List<PropertyModel> propertiesList = new ArrayList<>();
        try {
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                PropertyModel propertyModel = new PropertyModel(
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_SURFACE_AREA)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_BEDROOMS_COUNT)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_RENTAL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_STATUS)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_HAS_BALCONY))==1,
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_HAS_GARDEN))==1,
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_CONSTRUCTION_YEAR)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_AVAILABILITY_DATE)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndex(PROPERTY_COLUMN_CITY_ID)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_AGENCY_ID)),
                        cursor.getString(cursor.getColumnIndex(PROPERTY_COLUMN_ADVERTISE_DATE)),
                        new ArrayList<>()
                );
                propertiesList.add(propertyModel);
            } while (cursor.moveToNext());
        }
        } catch (Exception e) {
            Log.d("MySqliteError", "Error while trying to get properties list from database");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return propertiesList;
    }

    public void deleteAllNationalities() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NATIONALITY,null,null);
        sqLiteDatabase.close();
    }

    public void deleteAllCountries() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_COUNTRY,null,null);
        sqLiteDatabase.close();
    }

    @SuppressLint("Range")
    public CityModel getCityById(int cityId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_CITY,
                new String[]{CITY_COLUMN_ID,CITY_COLUMN_NAME,CITY_COLUMN_COUNTRY_ID},
                CITY_COLUMN_ID+"=?",
                new String[]{String.valueOf(cityId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
           CityModel city = new CityModel();
           city.setId(cursor.getInt(cursor.getColumnIndex(CITY_COLUMN_ID)));
           city.setCountryId(cursor.getInt(cursor.getColumnIndex(CITY_COLUMN_COUNTRY_ID)));
           city.setName(cursor.getString(cursor.getColumnIndex(CITY_COLUMN_NAME)));
           return  city;
        }
        return null;
    }

    public void deleteAllCities() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CITY,null,null);
        sqLiteDatabase.close();
    }

    public void insertCity(CityModel city) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_COLUMN_ID, city.getId());
        contentValues.put(CITY_COLUMN_NAME, city.getName());
        contentValues.put(CITY_COLUMN_COUNTRY_ID, city.getCountryId());
        sqLiteDatabase.insert(TABLE_CITY,null,contentValues);
    }

    @SuppressLint("Range")
    public List<CityModel> getCountryCities(int countryId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_CITY,
                new String[]{CITY_COLUMN_ID,CITY_COLUMN_NAME,CITY_COLUMN_COUNTRY_ID},
                CITY_COLUMN_COUNTRY_ID+"=?",
                new String[]{String.valueOf(countryId)},
                null,
                null,
                null
        );
        List<CityModel> citiesList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                CityModel city = new CityModel();
                city.setId(cursor.getInt(cursor.getColumnIndex(CITY_COLUMN_ID)));
                city.setCountryId(cursor.getInt(cursor.getColumnIndex(CITY_COLUMN_COUNTRY_ID)));
                city.setName(cursor.getString(cursor.getColumnIndex(CITY_COLUMN_NAME)));
                citiesList.add(city);
            }while (cursor.moveToNext());
            return citiesList;
        }
        return null;
    }

    @SuppressLint("Range")
    public CountryModel getCountryById(int countryId) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                TABLE_COUNTRY,
                new String[]{COUNTRY_COLUMN_ID,COUNTRY_COLUMN_NAME,COUNTRY_COLUMN_ZIP_CODE},
                COUNTRY_COLUMN_ID+"=?",
                new String[]{String.valueOf(countryId)},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            CountryModel countryModel = new CountryModel();
            countryModel.setCountryId(cursor.getInt(cursor.getColumnIndex(COUNTRY_COLUMN_ID)));
            countryModel.setName(cursor.getString(cursor.getColumnIndex(COUNTRY_COLUMN_NAME)));
            countryModel.setZipCode(cursor.getString(cursor.getColumnIndex(COUNTRY_COLUMN_ZIP_CODE)));
            return  countryModel;
        }
        return null;
    }
}
