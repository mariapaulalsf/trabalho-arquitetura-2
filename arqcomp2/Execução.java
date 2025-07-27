import java.util.ArrayList;
import java.util.List;

public class Execução {
    private List<Instrucao> instrucoes = new ArrayList<>();

    public void adicionarInstrucao(String linha) {
        if (linha != null && !linha.trim().isEmpty()) {
            Instrucao instrucao = new Instrucao(linha);
            instrucoes.add(instrucao);
        }
    }

    public int calculandoCiclos() {
        if (instrucoes.isEmpty()) return 0;

        int ciclos = 4; 
        for (int i = 0; i < instrucoes.size(); i++) {
            ciclos++;

            if (i > 0) {
                Instrucao atual = instrucoes.get(i);
                Instrucao anterior = instrucoes.get(i - 1);

                String regEscrita = anterior.getRegEscrita();
                List<String> regsLeitura = atual.getRegsLeitura();

                if (regEscrita != null && regsLeitura.contains(regEscrita)) {
                    if (anterior.isL) {
                        ciclos++;
                    }
                }
            }
        }

        return ciclos;
    }

    public List<String> gerarPipelineComBolhas() {
        List<String> saida = new ArrayList<>();

        for (int i = 0; i < instrucoes.size(); i++) {
            Instrucao atual = instrucoes.get(i);
            if (i > 0) {
                Instrucao anterior = instrucoes.get(i - 1);
                String regEscrita = anterior.getRegEscrita();
                List<String> regsLeitura = atual.getRegsLeitura();

                if (regEscrita != null && regsLeitura.contains(regEscrita) && anterior.isL) {
                    saida.add("NOP"); // Inserir bolha
                }
            }
            saida.add(atual.instrucaoOriginal);
        }

        return saida;
    }

    private static class Instrucao {
        String tipo;
        String opcode;
        String rs, rt, rd;
        String immediate;
        boolean isL;
        boolean isS;
        boolean isD;
        boolean isJ;
        String instrucaoOriginal;

        public Instrucao(String instrucao) {
            this.instrucaoOriginal = instrucao.trim();
            parseInstrucao(instrucao);
        }

        private void parseInstrucao(String instrucao) {
            instrucao = instrucao.trim()
                    .replaceAll("\\s+", " ")
                    .replace(",", " ")
                    .replace("(", " ")
                    .replace(")", " ");

            String[] partes = instrucao.split("\\s+");

            if (partes.length == 0)
                return;

            this.opcode = partes[0].toLowerCase();

            switch (opcode) {
                case "add":
                case "sub":
                case "and":
                case "or":
                case "xor":
                case "sll":
                case "srl":
                    this.tipo = "R";
                    parseInstrucaoR(partes);
                    break;

                case "lb":
                case "lh":
                case "lw":
                    this.tipo = "I";
                    this.isL = true;
                    parseInstrucaoL(partes);
                    break;

                case "sb":
                case "sh":
                case "sw":
                    this.tipo = "I";
                    this.isS = true;
                    parseInstrucaoS(partes);
                    break;

                case "addi":
                case "andi":
                case "ori":
                case "xori":
                case "lui":
                    this.tipo = "I";
                    parseInstrucaoI(partes);
                    break;

                case "beq":
                case "bne":
                case "blez":
                case "bgtz":
                    this.tipo = "I";
                    this.isD = true;
                    parseInstrucaoDesvio(partes);
                    break;

                case "j":
                    this.tipo = "J";
                    this.isJ = true;
                    break;
                case "jr":
                    this.tipo = "R";
                    this.isJ = true;
                    parseInstrucaoJR(partes);
                    break;

                default:
                    this.tipo = "DESCONHECIDO";
            }
        }

        private void parseInstrucaoR(String[] partes) {
            if (partes.length >= 4) {
                this.rd = partes[1];
                this.rs = partes[2];
                this.rt = partes[3];
            }
        }

        private void parseInstrucaoI(String[] partes) {
            if (partes.length >= 4) {
                this.rt = partes[1];
                this.rs = partes[2];
                this.immediate = partes[3];
            }
        }

        private void parseInstrucaoL(String[] partes) {
            if (partes.length >= 4) {
                this.rt = partes[1];
                this.immediate = partes[2];
                this.rs = partes[3];
            }
        }

        private void parseInstrucaoS(String[] partes) {
            if (partes.length >= 4) {
                this.rt = partes[1];
                this.immediate = partes[2];
                this.rs = partes[3];
            }
        }

        private void parseInstrucaoDesvio(String[] partes) {
            if (opcode.equals("blez") || opcode.equals("bgtz")) {
                if (partes.length >= 3) {
                    this.rs = partes[1];
                    this.immediate = partes[2];
                }
            } else {
                if (partes.length >= 4) {
                    this.rs = partes[1];
                    this.rt = partes[2];
                    this.immediate = partes[3];
                }
            }
        }

        private void parseInstrucaoJR(String[] partes) {
            if (partes.length >= 2) {
                this.rs = partes[1];
            }
        }

        public String getRegEscrita() {
            if (isS || isD || isJ)
                return null;

            switch (tipo) {
                case "R":
                    return rd;
                case "I":
                    return rt;
                default:
                    return null;
            }
        }

        public List<String> getRegsLeitura() {
            List<String> regs = new ArrayList<>();

            if (rs != null && !rs.equals("$zero") && !rs.equals("0")) {
                regs.add(rs);
            }

            if (rt != null && !rt.equals("$zero") && !rt.equals("0")) {
                if (isS || (!isL && tipo.equals("R"))) {
                    regs.add(rt);
                }
                if (isD && !opcode.equals("blez") && !opcode.equals("bgtz")) {
                    regs.add(rt);
                }
            }

            return regs;
        }
    }
}
