package TiposdeAtividades;

public class PerguntaAlterna {

    public int pontos;
    public String codPag;
    public String pergunta;
    public String alt1;
    public String alt2;
    public String alt3;
    public String alt4;
    public String altCerta;
    public String url;

    public PerguntaAlterna() {
    }

    public PerguntaAlterna(int pontos, String codPag, String pergunta, String alt1, String alt2, String alt3, String alt4, String altCerta, String url) {
        this.pontos = pontos;
        this.codPag = codPag;
        this.pergunta = pergunta;
        this.alt1 = alt1;
        this.alt2 = alt2;
        this.alt3 = alt3;
        this.alt4 = alt4;
        this.altCerta = altCerta;
        this.url = url;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public String getCodPag() {
        return codPag;
    }

    public void setCodPag(String codPag) {
        this.codPag = codPag;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getAlt1() {
        return alt1;
    }

    public void setAlt1(String alt1) {
        this.alt1 = alt1;
    }

    public String getAlt2() {
        return alt2;
    }

    public void setAlt2(String alt2) {
        this.alt2 = alt2;
    }

    public String getAlt3() {
        return alt3;
    }

    public void setAlt3(String alt3) {
        this.alt3 = alt3;
    }

    public String getAlt4() {
        return alt4;
    }

    public void setAlt4(String alt4) {
        this.alt4 = alt4;
    }

    public String getAltCerta() {
        return altCerta;
    }

    public void setAltCerta(String altCerta) {
        this.altCerta = altCerta;
    }

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url; }
}
