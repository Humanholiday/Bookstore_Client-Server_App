public class Menu
{
    //********** METHODS TO RETURN MENU HEADERS **************

    // returns main menu string
    public static String menu()
    {
        StringBuilder menu = new StringBuilder();
        // Creating the header for our menu
                menu.append("connected\n" +
                "-----------------------------------\n" +
                "| Welcome to the Online Book Store |\n" +
                "-----------------------------------\n" +
                "Please make a selection\n" +
                "1) Create a new database entry\n" +
                "2) Search the database\n" +
                "3) Update a database entry\n" +
                "4) Delete a database entry\n" +
                "Or type 'quit' to exit the application");

        return menu.toString();
    }


    // Method to run the main menu
    protected static String menuSelect(int choice) {
        switch (choice) {
            case 1:
                return create();
//                break;
            case 2:
                return search();
//                break;
            case 3:
                return update();
//                break;
            case 4:
                return delete();
//                break;
            default:
                return "Menu selection is out of range";
        }
    }

    public static String create()
    {
        // Creating the header for our menu
        String bookstoreMenu =
                ("-----------------------------\n" +
                "| Create a new database entry |\n" +
                "-----------------------------\n" +
                "Type TABLENAME ADD FIELDS.....\n" +
                "Examples -\n" +
                "book add 5436596 Fiesta Ernest_Hemingway HarperCollins English 4 10.99\n" +
                "customer add John Henry 016154309675 johnhenry@gmail.com\n" +
                "Or type 'return' to go back to the main menu or 'quit' to exit the application");

        return bookstoreMenu;
    }

    public static String search()
    {
        // Creating the header for our menu
        String bookstoreMenu =
                ("------------------------------\n" +
                "|   Search the database      |\n" +
                "------------------------------\n" +
                "Type TABLENAME SEARCH FIELD\n" +
                "Examples -\n" +
                "book search the count of monte cristo\n" +
                "customer search jack\n" +
                "Or type 'return' to go back to the main menu or 'quit' to exit the application");

        return bookstoreMenu;
    }

    public static String update()
    {
        // Creating the header for our menu
        String bookstoreMenu =
                ("------------------------------\n" +
                "|   Update a database entry   |\n" +
                "------------------------------\n" +
                "Type TABLENAME UPDATE FIELD WITH NEW_FIELD WHERE OTHER_FIELD IS *\n" +
                "Examples -\n" +
                "book update title the raod to the road where author is cormac_mccarthy\n" +
                "customer update firstname Jcak to Jack where email is jack@jack.com\n" +
                "Or type 'return' to go back to the main menu or 'quit' to exit the application");

        return bookstoreMenu;
    }

    public static String delete()
    {
        // Creating the header for our menu
        String bookstoreMenu =
                ("------------------------------\n" +
                        "|   Delete a database entry    |\n" +
                        "------------------------------\n" +
                        "Type TABLENAME DELETE FIELD\n" +
                        "Examples -\n" +
                        "book delete the count of monte cristo\n" +
                        "customer delete jack\n" +
                        "Or type 'return' to go back to the main menu or 'quit' to exit the application");

        return bookstoreMenu;
    }
}
