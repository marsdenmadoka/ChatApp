package marsmadoka98.gmail.whatsapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//CLASS TO ACCESS OUR FRAGMENTS
public class TabsFragmentAdapter extends FragmentPagerAdapter {


    public TabsFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) { //display accessing our fragments

        switch(position){

            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return  chatsFragment;

            case 1:
                 GroupsFragment groupsFragment = new GroupsFragment();
                return  groupsFragment;
            case 2:
                ContactsFragment contactsFragment = new ContactsFragment();
                return  contactsFragment;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3; //return 3 since we had 3 fragments
    }

    public CharSequence getPageTitle(int position){ //setting the fragment title

        switch(position){

            case 0:
                return  "chats";

            case 1:

                return  "Groups";
            case 2:

                return  "Contacts";

            default:
                return null;

        }


    }
}
