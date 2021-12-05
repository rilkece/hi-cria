package Classes;

public class PerfilAtividade {

    private String nomeAtividade;
    private long codAtividade;
    private boolean liberaAtividade;

    public PerfilAtividade() {
    }

    public PerfilAtividade(String nomeAtividade, long codAtividade,  boolean liberaAtividade) {
        this.nomeAtividade = nomeAtividade;
        this.codAtividade = codAtividade;
        this.liberaAtividade = liberaAtividade;
    }

    public String getNomeAtividade() {
        return nomeAtividade;
    }

    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    public long getCodAtividade() {
        return codAtividade;
    }

    public void setCodAtividade(long codAtividade) {
        this.codAtividade = codAtividade;
    }

    public boolean isLiberaAtividade() {
        return liberaAtividade;
    }

    public void setLiberaAtividade(boolean liberaAtividade) {
        this.liberaAtividade = liberaAtividade;
    }
}
