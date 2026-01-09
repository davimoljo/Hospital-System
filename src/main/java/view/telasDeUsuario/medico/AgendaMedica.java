package view.telasDeUsuario.medico;

import usuario.Medico;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaMedica {

    private final Medico medico;
    private final Map<LocalDate, List<SlotAtendimento>> agenda;

    public AgendaMedica(Medico medico) {
        this.medico = medico;
        this.agenda = new HashMap<>();
    }

    /**
     * Gera slots de atendimento para um dia específico
     */
    public void gerarSlotsDia(
            LocalDate data,
            LocalTime inicio,
            LocalTime fim,
            int duracaoMinutos) {

        if (duracaoMinutos <= 0) {
            throw new IllegalArgumentException("Duração inválida");
        }

        if (!inicio.isBefore(fim)) {
            throw new IllegalArgumentException("Horário inicial deve ser antes do final");
        }

        LocalTime atual = inicio;

        while (atual.isBefore(fim)) {
            LocalTime termino = atual.plusMinutes(duracaoMinutos);

            if (termino.isAfter(fim)) {
                break;
            }

            adicionarSlot(data, new SlotAtendimento(atual, termino));
            atual = termino;
        }
    }

    /**
     * Adiciona um slot garantindo que não haja sobreposição
     */
    public void adicionarSlot(LocalDate data, SlotAtendimento novoSlot) {
        agenda.putIfAbsent(data, new ArrayList<>());
        List<SlotAtendimento> slots = agenda.get(data);

        for (SlotAtendimento existente : slots) {
            if (sobrepoe(novoSlot, existente)) {
                throw new IllegalArgumentException(
                        "Conflito de horário na agenda do médico");
            }
        }

        slots.add(novoSlot);

        // mantém a agenda ordenada pelo horário de início
        slots.sort(Comparator.comparing(SlotAtendimento::getInicio));
    }

    /**
     * Verifica se dois slots se sobrepõem
     */
    private boolean sobrepoe(SlotAtendimento a, SlotAtendimento b) {
        return a.getInicio().isBefore(b.getFim()) &&
                b.getInicio().isBefore(a.getFim());
    }

    public List<SlotAtendimento> getSlotsDoDia(LocalDate data) {
        return agenda.getOrDefault(data, new ArrayList<>());
    }

    public Map<LocalDate, List<SlotAtendimento>> getAgendaCompleta() {
        return Map.copyOf(agenda); // evita modificação externa
    }

    public boolean possuiSlotsNoDia(LocalDate data) {
        return agenda.containsKey(data);
    }
}
