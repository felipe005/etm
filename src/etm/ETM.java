package etm;

import auxiliar.ComparadorDeParadas;
import auxiliar.GeradorDeId;
import auxiliar.Parada;
import auxiliar.RelatorioCustoDiario;
import auxiliar.RelatorioPorPeriodo;
import auxiliar.Rota;
import funcionarios.Funcionario;
import funcionarios.FuncionarioOperacional;
import java.util.ArrayList;
import veiculos.Veiculo;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import oficina.Oficina;
import passageiro.CartaoMag;

public class ETM {
    private String nome;
    private String id;
    
    private float tarifa;
    private Oficina oficina;
    
    private GeradorDeId idRotas;
    private GeradorDeId idVeiculos;
    private GeradorDeId idRelatorios;
    private GeradorDeId idParadas;
    private GeradorDeId idCartoes;
    private String [] tiposDeVeiculos;
    private ArrayList <Veiculo> veiculos;
    private ArrayList <Funcionario> funcionarios;
    private ArrayList <RelatorioCustoDiario> relatoriosDiarios;
    private ArrayList <Rota> rotas;
    private ArrayList <Parada> paradas;
    private ArrayList <CartaoMag> cartoes;
    private boolean paradasDesordenadas = true;
    
    public ETM(){
        this.tiposDeVeiculos = new String[]{"ÔNIBUS", "VAN", "MICRO-ÔNIBUS", "METRÔ", "VLT", "BRT"};
        relatoriosDiarios = new ArrayList();
        funcionarios = new ArrayList();
        veiculos = new ArrayList();
        rotas = new ArrayList();   
        paradas  = new ArrayList();
        cartoes = new ArrayList();
        idRotas = new GeradorDeId();
        idVeiculos = new GeradorDeId();
        idRelatorios = new GeradorDeId();
        idParadas = new GeradorDeId();
        idCartoes = new GeradorDeId();
        oficina = new Oficina();
    }
    
    public ETM(String nome,String id){
        this.tiposDeVeiculos = new String[]{"ÔNIBUS", "VAN", "MICRO-ÔNIBUS", "METRÔ", "VLT", "BRT"};
        this.nome = nome;
        this.id = id;
        relatoriosDiarios = new ArrayList();
        funcionarios = new ArrayList();
        veiculos = new ArrayList();
        rotas = new ArrayList();   
        paradas  = new ArrayList();
        cartoes = new ArrayList();
        idRotas = new GeradorDeId();
        idVeiculos = new GeradorDeId();
        idRelatorios = new GeradorDeId();
        idParadas = new GeradorDeId();
        idCartoes = new GeradorDeId();
        oficina = new Oficina();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setTarifa(Float tarifa){
        this.tarifa = tarifa;
    }
    
    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }
    
    public Float getTarifa(){
        return tarifa;
    }
    
    public void cadastrarFuncionario(Funcionario nFuncionario){
        funcionarios.add(nFuncionario);
    }
    
    public void cadastrarVeiculo(Veiculo nVeiculo){
        nVeiculo.setId(idVeiculos.gerarId());
        veiculos.add(nVeiculo);
    }

    public void cadastrarRota(Rota nRota){
        nRota.setId(idRotas.gerarId());
        rotas.add(nRota);
        paradasDesordenadas = true;
    }
    //revisar a possibilidade de colocar um id
    public void cadastrarRelatorio(RelatorioCustoDiario relatorio){
        relatoriosDiarios.add(relatorio);
    }
    
    public void cadastrarParada(Parada nParada){
        nParada.setId(idParadas.gerarId());
        paradas.add(nParada);
        paradasDesordenadas = true;
    }
    
    public void cadastrarCartao(CartaoMag cartao){
        cartao.setnCartao(idCartoes.gerarId());
        cartoes.add(cartao);
    }
    
    public void cadastrarVeiculoEmRota(String idRota, String idVeiculo){
        Rota rota = buscarRota(idRota);
        Veiculo veiculo = buscarVeiculo(idVeiculo);
        if(rota!=null&&veiculo!=null){
            if(!rota.veiculoEstaCadastrado(idVeiculo)){
                rota.cadastrarVeiculo(veiculo);
                veiculo.setMinhaRota(rota);
            }
            
        }
    }
    
    public void cadastrarParadaEmRota(String idRota, String idParada){
        Rota rota = buscarRota(idRota);
        Parada parada = buscarParada(idParada);
        if(rota != null){
            if(!rota.paradaEstaCadastrada(idParada)){
                rota.addParada(parada);
            }
        }
    }
    
    public void cadastrarFuncionarioEmVeiculo(String idVeiculo, String cpf){
        Veiculo veiculo = buscarVeiculo(idVeiculo);
        Funcionario fun = buscarFuncionario(cpf);
        if(veiculo!=null&&fun!=null){
            if(!veiculo.funcionarioEstaCadastrado(cpf)){
                veiculo.cadastrarFuncionario((FuncionarioOperacional)fun);
            }
        }
    }
    
    //Atualizar número de veículos nas rotas
    //Retirar veículo das rotas
    public void descadastrarVeiculo(String idVeiculo){
        Veiculo veiculo = buscarVeiculo(idVeiculo);
        if(veiculo != null){
            veiculos.remove(veiculo);
        }       
    } 
    // descadastrar rota do veículos tbm
    public void descadastrarRota(String idRota){
        Rota rota = buscarRota(idRota);
        if(rota != null){
            rotas.remove(rota);
            paradasDesordenadas = true;
        }
       
    }
    //Retirar parada das rotas
    public void descadastrarParada(String idParada){
        Parada parada = buscarParada(idParada);
        if(parada != null){
            paradas.remove(parada);
            paradasDesordenadas = true;
        }
    }
    
    /*
    repensar sobre a necessidade
    public void descadastrarRelatorio(RelatorioCustoDiario relatorio){
        relatoriosDiarios.remove(relatorio);
    }
    */
    public void descadastrarFucionario(String idFun){
        Funcionario fun = buscarFuncionario(idFun);
        if(fun != null){
            funcionarios.remove(fun);
        }   
    }
    
    public void descadastrarCartao(String idCartao){
        CartaoMag cartao = buscarCartao(idCartao);
        if(cartao != null){
            cartoes.remove(cartao);
        }
    }
    
    public void descadastrarVeiculoEmRota(String idRota, String idVeiculo){
        Rota rota = buscarRota(idRota);
        Veiculo veiculo = buscarVeiculo(idVeiculo);
        if(rota!=null&&veiculo!=null){
            rota.descadastrarVeiculo(veiculo);
            veiculo.setMinhaRota(null);
        }
    }
    
    public void descadastrarParadaEmRota(String idRota, String idParada){
        Rota rota = buscarRota(idRota);
        Parada parada = buscarParada(idParada);
        if(rota!=null&&parada!=null){
            rota.subParada(idParada);
        }
    }
    
    public void descadastrarFuncionarioEmVeiculo(String idVeiculo, String cpf){
        Veiculo veiculo = buscarVeiculo(idVeiculo);
        Funcionario fun = buscarFuncionario(cpf);
        
        if(veiculo!=null&&fun!=null){
            veiculo.descadastrarFuncionario(cpf);
        }
    }
     
    //Retorna o veículo  que possuir o id igual ao idBuscado
    public Veiculo buscarVeiculo(String idBuscado){
        Iterator<Veiculo> i = veiculos.iterator();
        Veiculo veiculo;
        while(i.hasNext()){
            veiculo = i.next();
            if(veiculo.getId().equals(idBuscado)){
                return veiculo;
            }
        }
        return null;
    }
    //Retorna a rota que possuir o id igual ao idBuscado
    public Rota buscarRota(String idBuscado){
        Iterator<Rota> i = rotas.iterator();
        Rota rota;
        while(i.hasNext()){
            rota = i.next();
            if(rota.getId().equals(idBuscado)){
                return rota;
            }
        }
        return null;
    }
    //Retorna o funcionário que possuir o CPF igual ao cpfBuscado
    public Funcionario buscarFuncionario(String cpfBuscado){
       Iterator<Funcionario> i = funcionarios.iterator();
        Funcionario funcionario;
        while(i.hasNext()){
           funcionario = i.next();
            if(funcionario.getCpf().equals(cpfBuscado)){
                return funcionario;
            }
        }
        return null;
    }
    //Retorna a parada que possuir o id igual ao idBuscado
    public Parada buscarParada(String idBuscado){
        Iterator<Parada> i = paradas.iterator();
        Parada parada;
        while(i.hasNext()){
            parada = i.next();
            if(parada.getId().equals(idBuscado)){
                return parada;
            }
        }
        return null;
    }
    
    public CartaoMag buscarCartao(String nCartao){
        Iterator<CartaoMag> i = cartoes.iterator();
        CartaoMag cartao;
        while(i.hasNext()){
            cartao = i.next();
            if(cartao.getnCartao().equals(nCartao)){
                return cartao;
            }
        }
        return null;
    }
    
    //atualizar veículo nas rotas
    public void atualizarVeiculo(Veiculo veiAtualizado){
        if (veiculos.contains(veiAtualizado))
            veiculos.set(veiculos.indexOf(veiAtualizado), veiAtualizado);
    }
    
    public void atualizarFuncionario(Funcionario funAtualizado){
        
        if (funcionarios.contains(funAtualizado)){
            Funcionario fun= funcionarios.get(funcionarios.indexOf(funAtualizado));
            fun.atualizar(funAtualizado);
        }
    }
    public void atualizarCartao(CartaoMag cartao){
        if(cartoes.contains(cartao)){
            cartoes.set(cartoes.lastIndexOf(cartao), cartao);
        }
    }
    
    public void atualizarRota(Rota rotaAtualizada){
        if(rotas.contains(rotaAtualizada)){
            Rota rota = rotas.get(rotas.indexOf(rotaAtualizada));
            rota.atualizar(rotaAtualizada);
        }
    }
    
    public void atualizarParada(Parada paradaAtualizada){
        if(paradas.contains(paradaAtualizada)){
            Parada parada = paradas.get(paradas.indexOf(paradaAtualizada));
            parada.atualizar(paradaAtualizada);
        }
    }
    
    //Retorna todos os veículos cadastrados
    public ArrayList<Veiculo> getVeiculosCadastrados(){
        return veiculos;
    }
    
    public ArrayList<Parada> getParadasCadastradas(){
        return paradas;
    }
    
    public ArrayList<Rota> getRotasCadastradas(){
        return rotas;
    }
    
    //Retorna todos os endereços das paradas de uma rota
    public Rota getEnderecos(String idRota){
        return buscarRota(idRota);
    }
    
    //Gera um relatório com os funcionários que mais se encaixam nos parâmetros fornecidos
    public ArrayList gerarRelatorioFuncionario(String sexo,int idade, float cargaHoraria, float renda){
        ArrayList <Funcionario> relatados= new ArrayList();
        int p;
        int pAnt=0;
        Iterator<Funcionario> i = funcionarios.iterator();
        Funcionario fun;
        while(i.hasNext()){
            p=0;
            fun = i.next();
            if(sexo.toUpperCase().equals(fun.getSexo())){
                p+=1;
            }
            if(fun.getIdade() == idade){
                p+=1;
            }
            if(fun.getCargaHoraria()==cargaHoraria){
                p+=1;
            }
            if(fun.getSalario()==renda){
                p+=1;
            } 
            if(p==pAnt){
                pAnt = p;
                relatados.add(fun);
            }
            else if(p>pAnt){
               pAnt = p;
               relatados.removeAll(relatados);
               relatados.add(fun);
            }   
        }
        return relatados;
    }
    
    //Gera um relatório com os custo de um veículo específico
    public String gerarRelatorioDeCustoVeiculo(String idVeiculo){
        String relatorioDeCustos="";
        Iterator<Veiculo> i = veiculos.iterator();
        Veiculo veiculo;
        while(i.hasNext()){
            veiculo = i.next();
            if(veiculo.getId().equals(idVeiculo)){
                relatorioDeCustos += "\nFUNCIONÁRIOS: "+veiculo.getCustoComFuncionarios()+"\nCOMBUSTÍVEL: "+veiculo.getCustoComCombustivel();
                return relatorioDeCustos;
            }
        }
        return null;
    }
    
    //Gera um relatório com os custo de uma rota específica
    public String gerarRelatorioDeCustoRota(String idRota){
        String relatorioDeCustos="";
        Iterator<Rota> i = rotas.iterator();
        Rota rota;
        while(i.hasNext()){
            rota = i.next();
            if(rota.getId().equals(idRota)){
                relatorioDeCustos += "\nFUNCIONÁRIOS: "+rota.getCustoComFuncionarios()+"\nCOMBUSTÍVEL: "+rota.getCustoComCombustivel();
                return relatorioDeCustos;
            }
        }
        return null;
    }
    //resolver problema com a receita dos veículos
    public void BalancoDodia(){
        for(int j=0;j<tiposDeVeiculos.length;j++){
            float custoFun=0;
            float custoComb=0;
            float custoManu=0;
            float custoIdosos=0;
            float custoIntegra=0;
            float custoEstu=0;
            float receita=0;
            int nPassageirosAtendidos=0;
            Iterator<Veiculo> i = veiculos.iterator();
            Veiculo veiculo;
            while(i.hasNext()){
                veiculo = i.next();
                if(veiculo.getStatus().equals("NORMAL")&&veiculo.getTipoDeVeiculo().equals(tiposDeVeiculos[j])){
                    custoFun += veiculo.getCustoComFuncionarios();
                    custoComb += veiculo.getCustoComCombustivel();  
                    custoManu += veiculo.getCustoManutencao();
                    custoIdosos += veiculo.getCustoIdosos();
                    custoIntegra += veiculo.getCustoIntegracao();
                    custoEstu += veiculo.getCustoEstudantes();
                    //receita += veiculo.getReceita();
                    nPassageirosAtendidos += veiculo.getnPassageirosAtendidos();
                    veiculo.zerar();
                }
            }
           
            RelatorioCustoDiario nRelatorio = new RelatorioCustoDiario(idRelatorios.gerarId(),tiposDeVeiculos[j],relatoriosDiarios.size()+1,custoComb, custoFun,custoManu,custoIdosos, custoIntegra, custoEstu,receita,nPassageirosAtendidos);
            relatoriosDiarios.add(nRelatorio);
        }   
    }
    
    public RelatorioPorPeriodo relatorioPorPeriodoFuncionarios(Calendar dataInicial, Calendar dataFinal){
        float custoFun=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                custoFun += relatorioDiario.getCustoFuncionarios();
            }   
        }
        if(custoFun>0){
            return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"FUNCIONÁRIOS",relatoriosDiarios.size()+1,0,custoFun,0,0,0,0,0,0);
        }
        return null;
    }
    
    public RelatorioPorPeriodo relatorioPorPeriodoCombustivel(Calendar dataInicial, Calendar dataFinal){
        float custoCombus=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                custoCombus += relatorioDiario.getCustoCombustivel();
            }   
        }
        if(custoCombus>0){
            return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"COMBUSTÍVEL",relatoriosDiarios.size()+1,custoCombus,0,0,0,0,0,0,0);
        }
        return null;
    }
    
    public RelatorioPorPeriodo relatorioPorPeriodoManutencao(Calendar dataInicial, Calendar dataFinal){
        float custoManu=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                custoManu += relatorioDiario.getCustoManutencao();
            }   
        }
        if(custoManu>0){
            return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"MANUTENÇÃO",relatoriosDiarios.size()+1,0,0,custoManu,0,0,0,0,0);
        }
        return null;
    }
    
    public RelatorioPorPeriodo relatorioPorPeriodoReceita(Calendar dataInicial, Calendar dataFinal){
        float receita=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                receita += relatorioDiario.getReceita();
            }   
        }
        if(receita>0){
            return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"RECEITA",0,0,0,0,0,0,0,receita,0);
        }
        return null;
    }
    
    public RelatorioPorPeriodo relatorioPorPeriodoIntegracao(Calendar dataInicial, Calendar dataFinal){
        float custoIntegracao=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                custoIntegracao += relatorioDiario.getCustoIntegracao();
            }   
        }
        if(custoIntegracao>0){
            return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"INTEGRAÇÃO",0,0,0,0,0,custoIntegracao,0,0,0);
        }
        return null;
    }
    
    public RelatorioPorPeriodo relatorioPorPeriodoEstudantes(Calendar dataInicial, Calendar dataFinal){
        float custoEstu=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                custoEstu += relatorioDiario.getCustoEstudantes();
            }   
        }
        if(custoEstu>0){
            return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"GRATUIDADE ESTUDANTIL",0,0,0,0,0,0,custoEstu,0,0);
        }
        return null;
    }
    
    public RelatorioPorPeriodo relatorioPorPeriodoIdosos(Calendar dataInicial, Calendar dataFinal){
        float custoIdosos=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                custoIdosos += relatorioDiario.getCustoIdosos();
            }   
        }
        if(custoIdosos>0){
            return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"GRATUIDADE POR IDADE",0,0,0,0,custoIdosos,0,0,0,0);
        }
        return null;
    }
    
    public RelatorioPorPeriodo getLucroOuPerda(Calendar dataInicial, Calendar dataFinal){
        float custoTotal=0; 
        float receita=0;
        long dataRelatorio;
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorioDiario;
        while(i.hasNext()){
            relatorioDiario = i.next();
            dataRelatorio = relatorioDiario.getDataEmMilisegundos();
            if(dataRelatorio>=dataInicial.getTimeInMillis() && dataRelatorio<=dataFinal.getTimeInMillis()){
                custoTotal += relatorioDiario.getEconomia();
                receita += relatorioDiario.getReceita();
            }   
        }
        return new RelatorioPorPeriodo(dataInicial,dataFinal,idRelatorios.gerarId(),"LUCRO OU PERDA",dataFinal,0,custoTotal,receita);
    }
    
    /*
    public void salvarRelatorio(RelatorioCustoDiario nRelatorio){
        if(proxPLrela<relatoriosDiarios.length){
            relatoriosDiarios[proxPLrela] = nRelatorio;
        }
        else{
            RelatorioCustoDiario [] temp = new RelatorioCustoDiario[relatoriosDiarios.length+3];
            for(int i=0;i<proxPLrela;i++){
                temp[i] = relatoriosDiarios[i];
            }
            temp[proxPLrela] = nRelatorio;
            relatoriosDiarios = temp;
        }
        proxPLrela += 1;
    }
    */
    
    //calcula e retorna a tarifa ideal de todos os veículos
    public String calcularTarifaIdealTveiculos(float percentDeLucro){
        String info="";
        Iterator<RelatorioCustoDiario> i = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorio;
        for(int j=0;j<relatoriosDiarios.size();j++){
            while(i.hasNext()){
                relatorio = i.next();
                if(tiposDeVeiculos[j].equals(relatorio.getRasaoRelatada())){
                    info +=calcularTarifaIdeal(percentDeLucro,relatorio.getRasaoRelatada())+"\n\n";
                }
            }
        }
        return info;
    }
    
    //calcula e retorna a tarifa ideal de um tipo de veículo
    public String calcularTarifaIdeal(float percentDeLucro,String tipoDeVeiculo){
        float custoTotal=0;
        int nPassageirosAtendidos=0;
        Iterator<RelatorioCustoDiario> relatorios = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorio;
        while(relatorios.hasNext()){
            relatorio = relatorios.next();
            if(relatorio.getRasaoRelatada().equals(tipoDeVeiculo.toUpperCase())){
                custoTotal += relatorio.getCustoTotal();
                nPassageirosAtendidos = relatorio.getnPassageirosAtendidos();
            }
        }
        tarifa=0;
        if(nPassageirosAtendidos>0){
            tarifa = (custoTotal/nPassageirosAtendidos)*(1+(percentDeLucro/100));
        }
        return "TIPO DE VEÍCULO: "+tipoDeVeiculo.toUpperCase()+"\nTARIFA IDEAL: "+tarifa;
    }
    
    private void ordenar(int ordem){
        if(paradasDesordenadas){
            if(ordem<0){
               Comparator<Parada> c = new ComparadorDeParadas().reversed();
               Collections.sort(paradas, c);
            }
            else{
                Comparator<Parada> c = new ComparadorDeParadas();
                Collections.sort(paradas,c);
            }
            
        }
    }
    
    public String getUsoDosPontos(int ordem){
        String info="";
        ordenar(ordem);
        Iterator<Parada> i = paradas.iterator();
        Parada parada;
        while(i.hasNext()){
            //System.out.println("dentro do laço");
            parada = i.next();
            info += "\nPONTO: "+parada.getEndereco()+"\nNº VEÍCULOS: "+parada.getnVeiculos();
        }
        return info;
    }
    

    public void mandarVeiculoParaOfici(String id){
       Veiculo veiculoDanificado = buscarVeiculo(id);
       if(veiculoDanificado != null){
            oficina.cadatrarVeiculo(veiculoDanificado);
       }
    }
    
    public double calcularDistancia(String idRota, Parada p1, Parada p2){
        Iterator<Rota> i = rotas.iterator();
        Rota rota = buscarRota(idRota);
        return rota.calcularDistancia(p1, p2);
    }
    
    public String mostrarRelatoriosDiarios(){
        String info="";
        Iterator<RelatorioCustoDiario> relatorios = relatoriosDiarios.iterator();
        RelatorioCustoDiario relatorio;
        while(relatorios.hasNext()){
            relatorio = relatorios.next();
            info += relatorio.toString()+"\n\n";
        }
        return info;
    }
    
    @Override
    public String toString(){
        return "NOME: "+nome+"\nID: "+id; 
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj!=null){
            if(obj instanceof ETM){
                if(((ETM)obj).id.equals(this.id)){
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }
}