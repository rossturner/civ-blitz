
const matchStates = {
    SIGNUPS: 'Signups',
    DRAFT: 'Draft',
    IN_PROGRESS: 'In progress',
    COMPLETED: 'Completed'
}

const matchStateToString = (enumValue) => {
    return matchStates[enumValue];
}

export  { matchStateToString };