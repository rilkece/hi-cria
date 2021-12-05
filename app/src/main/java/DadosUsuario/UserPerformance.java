package DadosUsuario;

public class UserPerformance {

    public long atividades;
    public long pontos;
    public String level;
    public long avatar;

    public UserPerformance() {
    }

    public UserPerformance(long atividades, long pontos, String level, long avatar) {
        this.atividades = atividades;
        this.pontos = pontos;
        this.level = level;
        this.avatar = avatar;
    }

    public long getAtividades() {
        return atividades;
    }

    public void setAtividades(long atividades) {
        this.atividades = atividades;
    }

    public long getPontos() {
        return pontos;
    }

    public void setPontos(long pontos) {
        this.pontos = pontos;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getAvatar() {
        return avatar;
    }

    public void setAvatar(long avatar) {
        this.avatar = avatar;
    }
}
