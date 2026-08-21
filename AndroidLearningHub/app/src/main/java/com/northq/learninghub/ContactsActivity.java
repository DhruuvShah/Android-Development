package com.northq.learninghub;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit 4 Theory: "Content Providers" — Unit 4 Practical: "Access and display
 * contacts using ContentProvider."
 *
 * Reads from the SYSTEM Contacts provider (android.provider.ContactsContract)
 * via ContentResolver.query() — the standard content:// URI mechanism. This
 * is deliberately the read-only system-provider variant of the syllabus
 * item; ExpenseDbHelper (Unit 4 CRUD practical) shows the "own database"
 * side of the same underlying idea.
 */
public class ContactsActivity extends AppCompatActivity {

    private final List<Contact> contacts = new ArrayList<>();
    private ContactAdapter adapter;

    private final ActivityResultLauncher<String> requestContactsPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) loadContacts();
                else Toast.makeText(this, "Contacts permission is required to show this list", Toast.LENGTH_SHORT).show();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.contactsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, contacts);
        recyclerView.setAdapter(adapter);

        if (PermissionUtils.isGranted(this, Manifest.permission.READ_CONTACTS)) {
            loadContacts();
        } else {
            requestContactsPermission.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private void loadContacts() {
        contacts.clear();
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            while (cursor.moveToNext()) {
                String name = nameIdx >= 0 ? cursor.getString(nameIdx) : "(Unknown)";
                String number = numberIdx >= 0 ? cursor.getString(numberIdx) : "";
                contacts.add(new Contact(name, number));
            }
            cursor.close();
        }

        if (contacts.isEmpty()) {
            Toast.makeText(this, "No contacts found on this device/emulator", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }
}
