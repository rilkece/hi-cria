package DadosUsuario;

public class UserWeekPerformance {
    public String name;
    public String surname;
    public String school;
    public String weekScore;
    public String mat;

    public UserWeekPerformance() {
    }

    public UserWeekPerformance(String name, String surname, String school, String weekScore, String mat) {
        this.name = name;
        this.surname = surname;
        this.school = school;
        this.weekScore = weekScore;
        this.mat = mat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getWeekScore() {
        return weekScore;
    }

    public void setWeekScore(String weekScore) {
        this.weekScore = weekScore;
    }

    public String getMat() { return mat;  }

    public void setMat(String mat) {  this.mat = mat; }
}
