package DadosUsuario;

public class UserGamePerformance {

    public String name;
    public String surname;
    public String school;
    public String gameScore;
    public String mat;

    public UserGamePerformance() {
    }

    public UserGamePerformance(String name, String surname, String school, String gameScore, String mat) {
        this.name = name;
        this.surname = surname;
        this.school = school;
        this.gameScore = gameScore;
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

    public String getgameScore() {
        return gameScore;
    }

    public void setgameScore(String gameScore) {
        this.gameScore = gameScore;
    }

    public String getMat() { return mat;  }

    public void setMat(String mat) {  this.mat = mat; }
}
