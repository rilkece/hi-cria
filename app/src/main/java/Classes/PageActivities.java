package Classes;

public class PageActivities {

    public int numPage;
    public String codDay;
    public  String codAtv;

    public PageActivities() {
    }

    public PageActivities(int numPage, String codDay, String codAtv) {
        this.numPage = numPage;
        this.codDay = codDay;
        this.codAtv = codAtv;

    }

    public int getNumPage() {    return numPage; }

    public void setNumPage(int numPage) {
        this.numPage = numPage;
    }

    public String getCodDay() {
        return codDay;
    }

    public void setCodDay(String codDay) {
        this.codDay = codDay;
    }

    public String getCodAtv() {
        return codAtv;
    }

    public void setCodAtv(String codAtv) {
        this.codAtv = codAtv;
    }
}

