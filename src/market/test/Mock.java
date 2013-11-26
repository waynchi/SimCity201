package market.test;

public class Mock {
        public String name;
        public EventLog log = new EventLog();

        //have a log here in order to check message send by cashier to the other mocks

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
