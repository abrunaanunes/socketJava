package classes;

public class CategoryList {
	public static Category categories[];
	
	public CategoryList()
	{
		this.categories = new Category[8];
        this.initializeList();
	}
	
	public void initializeList() {

    	Category category0 = new Category(0, "Programador");
    	Category category1 = new Category(1, "Eletricista");
    	Category category2 = new Category(2, "Mecânico");
    	Category category3 = new Category(3, "Cientista");
    	Category category4 = new Category(4, "Professor");
    	Category category5 = new Category(5, "Analista");
    	Category category6 = new Category(6, "Gamer");
    	Category category7 = new Category(7, "Streamer");

        this.categories[0] = category0;
        this.categories[1] = category1;
        this.categories[2] = category2;
        this.categories[3] = category3;
        this.categories[4] = category4;
        this.categories[5] = category5;
        this.categories[6] = category6;
        this.categories[7] = category7;

    }
	
	public static Category getCategory(int id) {
        int i;
        for (i = 0; i < categories.length; i++) {

            if (categories[i].getId() == id) {

                return categories[i];

            }

        }
        return null;
    }
}
