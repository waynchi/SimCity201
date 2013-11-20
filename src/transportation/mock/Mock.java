package transportation.mock;

public class Mock {

	/**
	 * @param args
	 */
	private String name;

        public Mock(String name) {
                this.name = name;
        }

        public String getName() {
                return name;
        }

        public String toString() {
                return this.getClass().getName() + ": " + name;
        }

}
