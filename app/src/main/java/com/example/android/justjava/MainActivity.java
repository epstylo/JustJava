package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity++;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as a toast
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Find the user's name
        String nameText = (((EditText) findViewById(R.id.name)).getText()).toString();
        // Figure out if the user wants whipped cream and/or chocolate
        boolean hasWhippedCream = ((CheckBox) findViewById(R.id.whipped_cream_checkbox)).isChecked();
        boolean hasChocolate = ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();
        // Calculate the price of the order
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        // Generate order summary text
        String orderSummary = createOrderSummary(price, nameText, hasWhippedCream, hasChocolate);

        // Send order summary intent to email application
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + nameText);
        intent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @param hasWhippedCream as a topping
     * @param hasChocolate    as a topping
     * @return the price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int basePrice = 5;

        // Add $1 if the user wants whipped cream
        if (hasWhippedCream) {
            basePrice += 1;
        }
        // Add $2 if the user wants chocolate
        if (hasChocolate) {
            basePrice += 2;
        }
        return basePrice * quantity;
    }

    /**
     * Creates a summary of the order
     *
     * @param price of the order
     * @param hasWhippedCream checkbox for whipped cream
     * @return the order summary
     */
    private String createOrderSummary(int price, String name, boolean hasWhippedCream, boolean hasChocolate) {
        String summary = "Name: " + name + "\n" +
                "Add whipped cream? " + hasWhippedCream + "\n" +
                "Add chocolate? " + hasChocolate + "\n" +
                "Quantity: " + quantity + "\n" +
                "Total: $" + price + "\n" +
                "Thank you!";
        return summary;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}