package com.example.lenovo.inventoryappstage1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.lenovo.inventoryappstage1.data.Contract;

public class ProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentProductUri;

    private TextView mProductNameViewText;
    private TextView mProductPriceViewText;
    private TextView mProductQuantityViewText;
    private TextView mProductSupplieNameSpinner;
    private TextView mProductSupplierPhoneNumberViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mProductNameViewText = findViewById(R.id.product_name);
        mProductPriceViewText = findViewById(R.id.product_price);
        mProductQuantityViewText = findViewById(R.id.product_quantity);
        mProductSupplieNameSpinner = findViewById(R.id.product_supplier);
        mProductSupplierPhoneNumberViewText = findViewById(R.id.product_contact);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();
        if (mCurrentProductUri == null) {
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        Log.d("message", "onCreate ViewActivity");


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                Contract.ProductEntry._ID,
                Contract.ProductEntry.COLUMN_PRODUCT_NAME,
                Contract.ProductEntry.COLUMN_PRODUCT_PRICE,
                Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                Contract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME,
                Contract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

            final int idColumnIndex = cursor.getColumnIndex(Contract.ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(Contract.ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NUMBER);

            String currentName = cursor.getString(nameColumnIndex);
            final int currentPrice = cursor.getInt(priceColumnIndex);
            final int currentQuantity = cursor.getInt(quantityColumnIndex);
            int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
            final int currentSupplierPhone = cursor.getInt(supplierPhoneColumnIndex);

            mProductNameViewText.setText(currentName);
            mProductPriceViewText.setText(Integer.toString(currentPrice));
            mProductQuantityViewText.setText(Integer.toString(currentQuantity));
            mProductSupplierPhoneNumberViewText.setText(Integer.toString(currentSupplierPhone));


            switch (currentSupplierName) {
                case Contract.ProductEntry.SUPPLIER_AMAZON:
                    mProductSupplieNameSpinner.setText(getText(R.string.supplier_amazon));
                    break;
                case Contract.ProductEntry.SUPPLIER_EBAY:
                    mProductSupplieNameSpinner.setText(getText(R.string.supplier_amazon));
                    break;
                default:
                    mProductSupplieNameSpinner.setText(getText(R.string.supplier_ebay));
                    break;
            }

            Button productDecreaseButton = findViewById(R.id.decrease_button);
            productDecreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCount(idColumnIndex, currentQuantity);
                }
            });

            Button productIncreaseButton = findViewById(R.id.increase_button);
            productIncreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCount(idColumnIndex, currentQuantity);
                }
            });


            Button phoneButton = findViewById(R.id.phone_button);
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = String.valueOf(currentSupplierPhone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            });

        }
    }


    public void decreaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity - 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.decrease_quantity), Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
        } else {
            Toast.makeText(this, getString(R.string.empty_stock), Toast.LENGTH_SHORT).show();
        }
    }

    public void increaseCount(int productID, int productQuantity) {
        productQuantity = productQuantity + 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
            Toast.makeText(this, getString(R.string.increase_quantity), Toast.LENGTH_SHORT).show();

            Log.d("Log msg", " - productID " + productID + " - quantity " + productQuantity + " , decreaseCount has been called.");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private void updateProduct(int productQuantity) {
        Log.d("message", "updateProduct at ViewActivity");

        if (mCurrentProductUri == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(Contract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);

        if (mCurrentProductUri == null) {
            Uri newUri = getContentResolver().insert(Contract.ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.product_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete_book:
                showDeleteConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {

        if (mCurrentProductUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
