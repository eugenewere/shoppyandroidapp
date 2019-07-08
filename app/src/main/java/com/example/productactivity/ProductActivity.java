package com.example.productactivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    EditText productName,productDescription,productPrice;
    Button addProduct, emailbtn;
    Spinner categorySpinner;
    ImageView productImage;
    private Object Bitmap;
    private static final int REQUEST_CODE = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        productName=(EditText) findViewById(R.id.product_name);
        productDescription=(EditText) findViewById(R.id.product_description);
        productPrice=(EditText) findViewById(R.id.product_price);
        categorySpinner=(Spinner) findViewById(R.id.category_spinner);
        productImage=(ImageView) findViewById(R.id.product_image);
        addProduct=(Button) findViewById(R.id.product_button) ;
        emailbtn = (Button) findViewById(R.id.email);

        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sendEmail();
            }
        });


//        onclick image
        productImage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent eugene = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(eugene, REQUEST_CODE);
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isErrors()){
                    Toast.makeText(ProductActivity.this, "Please ensure you fill all the fields before proceeding", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ProductActivity.this, "Item will be added", Toast.LENGTH_SHORT).show();
                }
            }
        });
        populateSpinner();
        Intent i = getIntent();
        int position = i.getIntExtra(ProductListAdapter.CURRENT_POSITION_VALUE,-2);
        if(position!=-2)
        {
            Product  product = ProductListActivity.mProductArrayList.get(position);
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            productPrice.setText(product.getPrice());
            productImage.setImageResource(product.getImage());
        }

    }
    public boolean isErrors(){
        String name=productName.getText().toString();
        String price=productPrice.getText().toString();
        String description=productDescription.getText().toString();

        if (TextUtils.isEmpty(name)){
            productName.setError("Fill in the field");
            return true;
        }
        if (TextUtils.isEmpty(price)){
            productPrice.setError("Put in the price");
            return true;
        }
        if (TextUtils.isEmpty(description)){
            productDescription.setError("Please use put the description");
            return true;
        }

        else{
            return false;
        }


    }

    public void populateSpinner() {
     List<String> spinnerArray = new ArrayList<String>();
     spinnerArray.add("Category");
     spinnerArray.add("Electronics");
     spinnerArray.add("Beauty products");
     spinnerArray.add("Kitchen Products");
     spinnerArray.add("Kids ware");

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

}
// fetching the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode== REQUEST_CODE && data!=null){
            Bundle extras =data.getExtras();
            Bitmap b =(Bitmap) extras.get("data");
            productImage.setImageBitmap(b);
        }
    }
    private void sendEmail(){
        String subject = productName.getText().toString();
        String text ="i noticed an issue with the product"+subject;
        Intent i= new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc2822");
        i.putExtra(Intent.EXTRA_SUBJECT,"Faulty system");
        i.putExtra(Intent.EXTRA_TEXT,"System not working");
        startActivity(i);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();
        switch(itemId)
        {
            case R.id.admin_email: {
                sendEmail();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

}
