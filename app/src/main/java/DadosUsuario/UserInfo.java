package DadosUsuario;

public class UserInfo {
    public String matricula;
    public String nome;
    public String sobrenome;
    public String emailuser;
    public String tele;
    public String escola;
    public String func;
    public String dianasci;
    public String mesnasci;
    public String anonasci;
    public String sexo;
    public long tipoUser;
    public long utbCRIA;


    public UserInfo(){

    }

    public UserInfo(String matricula, String nome, String sobrenome, String emailuser, String tele, String escola,
                    String func, String dianasci, String mesnasci, String anonasci, String sexo, long tipoUser, long utbCRIA ){
        this.matricula = matricula;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.emailuser = emailuser;
        this.tele = tele;
        this.escola = escola;
        this.func = func;
        this.dianasci = dianasci;
        this.mesnasci = mesnasci;
        this.anonasci = anonasci;
        this.sexo = sexo;
        this.tipoUser = tipoUser;
        this.utbCRIA = utbCRIA;

    }

    public String getMatricula(){return matricula;}

    public String getNome() {
        return nome;
    }

    public String getSobrenome() { return sobrenome;}

    public String getEmailuser() {
        return emailuser;
    }

    public String getTele() {
        return tele;
    }

    public String getEscola() { return escola; }

    public String getFunc() { return func; }

    public String getDianasci(){return  dianasci;}

    public  String getMesnasci(){return mesnasci;}

    public String getAnonasci(){return anonasci;}

    public String getSexo() {
        return sexo;
    }

    public void setMatricula(String matricula) { this.matricula = matricula; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public void setEscola(String escola) { this.escola = escola; }

    public void setFunc(String func) { this.func = func; }

    public void setDianasci(String dianasci) {this.dianasci = dianasci;}

    public void setMesnasci(String mesnasci) {this.mesnasci = mesnasci;}

    public void setAnonasci(String anonasci) {this.anonasci = anonasci;}

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public long getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(long tipoUser) {
        this.tipoUser = tipoUser;
    }

    public long getUtbCRIA() {
        return utbCRIA;
    }

    public void setUtbCRIA(long utbCRIA) {
        this.utbCRIA = utbCRIA;
    }
}
