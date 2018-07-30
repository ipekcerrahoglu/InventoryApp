package com.example.lenovo.inventoryappstage1.data;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {

    private Contract() {
    }


    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.lenovo.inventoryappstage1";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.lenovo.products/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PRODUCTS = "products";

    /**
     * Inner class that defines constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    public static final class ProductEntry implements BaseColumns {

        /**
         * The content URI to access the pet data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * Name of database table for products
         */
        public final static String TABLE_NAME = "products";

        /**
         * Unique ID number for the product (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "name";

        /**
         * Breed of the pet.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_PRODUCT_PRICE = "price";


        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplierPhoneNumber";


        /**
         * Possible values for the supplierName of the product.
         */
        public static final int SUPPLIER_AMAZON = 0;
        public static final int SUPPLIER_EBAY = 1;


        public static boolean isValidSupplier(int supplier) {
            if (supplier == SUPPLIER_AMAZON || supplier == SUPPLIER_EBAY) {
                return true;
            }
            return false;
        }
    }
}

