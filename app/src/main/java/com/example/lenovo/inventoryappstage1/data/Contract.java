package com.example.lenovo.inventoryappstage1.data;


import android.provider.BaseColumns;

    public final class Contract {

        private Contract(){}

        public static abstract class ProductEntry implements BaseColumns {

            public static final String TABLE_NAME = "items";

            public static final String _ID = BaseColumns._ID;
            public static final String COLUMN_PRODUCT_NAME = "name";
            public static final String COLUMN_PRODUCT_PRICE = "price";
            public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
            public static final String COLUMN_PRODUCT_SUPPLIER_NAME = "supplierName";
            public static final String COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER = "supplierPhoneNumber";
        }
    }

