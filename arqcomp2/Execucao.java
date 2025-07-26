public class Execucao {
    private static class Instrucao {
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
        }
    }
}
