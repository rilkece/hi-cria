package TiposdeAtividades;

public class ButtonsAlterna {

    public int pontos;
    public String codPag;
    public String pergunta;
    public String instruction;
    public String alt1;
    public String alt2;
    public String alt3;
    public String alt4;
    public String alt5;
    public String alt6;
    public String alt7;
    public String alt8;
    public String alt9;
    public String altCerta;
    public String url;
    public String resposta;

    public ButtonsAlterna() {
    }

    public ButtonsAlterna(int pontos, String codPag, String pergunta, String instruction, String alt1, String alt2, String alt3, String alt4, String alt5, String alt6, String alt7, String alt8, String alt9, String altCerta, String url, String resposta) {
        this.pontos = pontos;
        this.codPag = codPag;
        this.pergunta = pergunta;
        this.instruction = instruction;
        this.alt1 = alt1;
        this.alt2 = alt2;
        this.alt3 = alt3;
        this.alt4 = alt4;
        this.alt5 = alt5;
        this.alt6 = alt6;
        this.alt7 = alt7;
        this.alt8 = alt8;
        this.alt9 = alt9;
        this.altCerta = altCerta;
        this.url = url;
        this.resposta = resposta;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.pergunta = instruction;
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

    public String getAlt5() {
        return alt5;
    }

    public void setAlt5(String alt5) {
        this.alt5 = alt5;
    }

    public String getAlt6() {
        return alt6;
    }

    public void setAlt6(String alt6) {
        this.alt6 = alt6;
    }

    public String getAlt7() {
        return alt7;
    }

    public void setAlt7(String alt7) {
        this.alt7 = alt7;
    }

    public String getAlt8() {
        return alt8;
    }

    public void setAlt8(String alt8) {
        this.alt8 = alt8;
    }

    public String getAlt9() {
        return alt9;
    }

    public void setAlt9(String alt9) {
        this.alt9 = alt9;
    }

    public String getAltCerta() {
        return altCerta;
    }

    public void setAltCerta(String altCerta) {
        this.altCerta = altCerta;
    }

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url; }

    public String getResposta() { return resposta;}

    public void setResposta(String resposta) { this.resposta = resposta; }
}
