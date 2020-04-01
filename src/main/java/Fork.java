import static java.lang.Thread.sleep;

class Fork {

    boolean taken;
    int id;

    void put() {
        // Fork is placed back on the table. -> status: not taken
        taken = false;
    }

    void get() throws InterruptedException {
        // Fork is taken from the table. -> status: taken
        while (taken) {
            sleep(10); // wait until the fork is back on the table.
        }
        taken = true;
    }

    public boolean isTaken() {
        return taken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}