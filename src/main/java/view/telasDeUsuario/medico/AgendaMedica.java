package view.telasDeUsuario.medico;

import utilitarios.Data;
import utilitarios.Hora;
import usuario.Medico;

import java.util.*;

public class AgendaMedica {

    private Medico medico;
    private Map<Data, List<SlotAtendimento>> agenda;

    public AgendaMedica(Medico medico) {
        this.medico = medico;
        this.agenda = new HashMap<>();
    }

    public void gerarSlotsDia(
            Data data,
            Hora inicio,
            Hora fim,
            int duracaoMinutos) {

        if (duracaoMinutos <= 0)
            throw new IllegalArgumentException("Duração inválida");

        Hora atual = inicio;

        while (atual.compareTo(fim) < 0) {
            Hora termino = atual.adicionarMinutos(duracaoMinutos);

            if (termino.compareTo(fim) > 0)
                break;

            adicionarSlot(
                    data,
                    new SlotAtendimento(atual, termino));

            atual = termino;
        }
    }

    public void adicionarSlot(Data data, SlotAtendimento novoSlot) {
        agenda.putIfAbsent(data, new ArrayList<>());
        List<SlotAtendimento> slots = agenda.get(data);

        for (SlotAtendimento existente : slots) {
            if (sobrepoe(novoSlot, existente)) {
                throw new IllegalArgumentException(
                        "Conflito de horário na agenda do médico");
            }
        }

        slots.add(novoSlot);

        // mantém agenda ordenada por horário
        slots.sort(Comparator.comparing(SlotAtendimento::getInicio));
    }

    private boolean sobrepoe(SlotAtendimento a, SlotAtendimento b) {
        return a.getInicio().compareTo(b.getFim()) < 0 &&
                b.getInicio().compareTo(a.getFim()) < 0;
    }

    public List<SlotAtendimento> getSlotsDoDia(Data data) {
        return agenda.getOrDefault(data, new ArrayList<>());
    }

    public Map<Data, List<SlotAtendimento>> getAgendaCompleta() {
        return agenda;
    }

    public boolean possuiSlotsNoDia(Data data) {
        return agenda.containsKey(data);
    }
}
